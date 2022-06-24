package ti.camerax

import android.annotation.SuppressLint
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.os.Build
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExtendableBuilder
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import org.appcelerator.kroll.KrollDict
import org.appcelerator.titanium.TiApplication
import org.appcelerator.titanium.proxy.TiViewProxy
import org.appcelerator.titanium.view.TiUIView
import java.util.concurrent.Executors


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CameraView(proxy: TiViewProxy) : TiUIView(proxy) {

    private var previewView: PreviewView
    private lateinit var view: View
    var lensFacing = CameraSelector.LENS_FACING_BACK
    private var imageView: ImageView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var cameraProvider: ProcessCameraProvider
    var autoFocus: Boolean = true
    var torchMode: Int = 0
    var formatList: MutableList<Int> = mutableListOf<Int>()

    override fun processProperties(d: KrollDict) {
        super.processProperties(d)
    }

    init {
        val inflater = LayoutInflater.from(proxy.activity)
        view = inflater.inflate(R.layout.camera_layout, null)
        previewView = view.findViewById<PreviewView>(R.id.view_finder)
        imageView = view.findViewById<ImageView>(R.id.imageView)
        setNativeView(view)
        fireEvent("ready", KrollDict())
    }

    @SuppressLint("RestrictedApi")
    fun startCamera() {

        cameraProviderFuture = ProcessCameraProvider.getInstance(proxy.activity)
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()

            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            val cameraExecutor = Executors.newSingleThreadExecutor()
            val previewBuilder: Preview.Builder = Preview.Builder()

            // auto focus off
            if (!autoFocus) {
                val focusDistance = 0f
                setFocusDistance(previewBuilder, focusDistance)
            }


            var preview: Preview = previewBuilder.build()
            var camera = cameraProvider.bindToLifecycle(
                TiApplication.getAppCurrentActivity() as LifecycleOwner,
                cameraSelector,
                preview
            )

            val scaleGestureDetector = ScaleGestureDetector(TiApplication.getAppCurrentActivity(),
                object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    override fun onScale(detector: ScaleGestureDetector): Boolean {
                        val scale =
                            camera.cameraInfo.zoomState.value!!.zoomRatio * detector.scaleFactor
                        camera.cameraControl.setZoomRatio(scale)
                        return true
                    }
                })

            previewView.setOnTouchListener { view, event ->
                view.performClick()
                scaleGestureDetector.onTouchEvent(event)
                return@setOnTouchListener true
            }
            previewView.implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            preview.setSurfaceProvider(previewView.surfaceProvider)

            // torch mode
            if (torchMode == TiCameraxModule.TORCH_MODE_ON) {
                camera.cameraControl.enableTorch(true)
            } else {
                camera.cameraControl.enableTorch(false)
            }

            view.findViewById<ImageButton>(R.id.camera_switch_button).let {
                // Listener for button used to switch cameras. Only called if the button is enabled
                it.setOnClickListener {
                    lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                        CameraSelector.LENS_FACING_BACK
                    } else {
                        CameraSelector.LENS_FACING_FRONT
                    }
                    // Re-bind use cases to update selected camera
                    startCamera()
                }
            }

        }, ContextCompat.getMainExecutor(proxy.activity))
    }


    @SuppressLint("UnsafeOptInUsageError")
    fun setFocusDistance(builder: ExtendableBuilder<*>?, distance: Float) {
        val extender: Camera2Interop.Extender<*> =
            Camera2Interop.Extender<Any>(builder as ExtendableBuilder<Any>)
        extender.setCaptureRequestOption(
            CaptureRequest.CONTROL_AF_MODE,
            CameraMetadata.CONTROL_AF_MODE_OFF
        )
        extender.setCaptureRequestOption(CaptureRequest.LENS_FOCUS_DISTANCE, distance)
    }


    fun stopCamera() {
        cameraProvider.unbindAll()
    }

    fun switchCamera(frontCam: Boolean) {
        if (frontCam) {
            lensFacing = CameraSelector.LENS_FACING_FRONT
        } else {
            lensFacing = CameraSelector.LENS_FACING_BACK
        }
    }

    fun setTorch(param: Int) {
        torchMode = param
    }

    fun setFormat(myList: MutableList<Int>) {
        formatList = myList
    }


}
