package watermark

class InformAndTerminateException(msg: String, cause: Throwable?) : Exception(msg, cause) {
    constructor(msg: String) : this(msg, null)
}