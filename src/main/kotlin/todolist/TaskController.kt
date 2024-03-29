package todolist

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import spark.Request
import spark.Route
import spark.Spark.halt

class TaskController(
    private val objectMapper: ObjectMapper,
    private val taskRepository: TaskRepository
) {

    fun index(): Route = Route { request, response ->
        taskRepository.findAll()
    }

    fun create(): Route = Route { req, res ->
        val request: TaskCreateRequest =
            objectMapper.readValue(req.bodyAsBytes()) ?: throw halt(400)
        val task = taskRepository.create(request.content)
        res.status(201)
        task
    }

    fun show(): Route = Route { req, res ->
        req.task ?: throw halt(404)
    }

    fun destroy(): Route = Route { req, res ->
        val task = req.task ?: throw halt(404)
        taskRepository.delete(task)
        res.status(204)
    }

    fun update(): Route = Route { req, res ->
        val request: TaskUpdateRequest =
            objectMapper.readValue(req.bodyAsBytes()) ?: throw halt(400)
        val task = req.task ?: throw halt(404)
        val newtask = task.copy(
            content = request.content ?: task.content,
            done = request.done ?: task.done
        )
        taskRepository.update(newtask)
        res.status(204)
    }

    private val Request.task: Task?
        get() {
            val id: Long? = params("id").toLongOrNull()
            return id?.let(taskRepository::findById)
        }
}
