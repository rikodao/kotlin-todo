package todolist

import com.fasterxml.jackson.annotation.JsonProperty

data class TaskUpdateRequest (
    @JsonProperty("content", required = true) val content: String?,
    @JsonProperty("done", required = true) val done: Boolean?
)
