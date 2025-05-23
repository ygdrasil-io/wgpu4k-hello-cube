import ffi.globalMemory
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ygdrasil.wgpu.WGPULogCallback
import io.ygdrasil.wgpu.WGPULogLevel_Debug
import io.ygdrasil.wgpu.WGPULogLevel_Error
import io.ygdrasil.wgpu.WGPULogLevel_Info
import io.ygdrasil.wgpu.WGPULogLevel_Trace
import io.ygdrasil.wgpu.WGPULogLevel_Warn
import io.ygdrasil.wgpu.wgpuSetLogCallback
import io.ygdrasil.wgpu.wgpuSetLogLevel

private val logger = KotlinLogging.logger {}

val callback = WGPULogCallback.allocate(globalMemory) { level, cMessage, userdata ->
    val message = cMessage?.data?.toKString(cMessage.length) ?: "empty message"
    when (level) {
        WGPULogLevel_Error -> logger.error { message }
        WGPULogLevel_Warn -> logger.warn { message }
        WGPULogLevel_Info -> logger.info { message }
        WGPULogLevel_Debug -> logger.debug { message }
        WGPULogLevel_Trace -> logger.trace { message }
        else -> logger.warn { "Unknown log level $level with message $message" }
    }
}

fun initLog() {
    wgpuSetLogLevel(WGPULogLevel_Trace)
    wgpuSetLogCallback(callback, globalMemory.bufferOfAddress(callback.handler).handler)
}

