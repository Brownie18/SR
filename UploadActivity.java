package com.example.ridge.upmod;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.HandlerThread;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.view.CollapsibleActionView;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Handler;


public class UploadActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA = 0;
    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAIT_LOCK = 1;
    private int aState;
    private ImageView aPhotoTakenImageView;
    private String aImageLocation = "";
    private String GALLERY_LOCATION = "image gallery";
    private File aGalleryFolder;
    private static LruCache<String, Bitmap> aMemoryCache;
    Button button;
    Button button2;
    Button button3;
    private String aCameraId;
    private Size aPreviewSize;
    private TextureView aTextureView;
    private TextureView.SurfaceTextureListener ViewFinderTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    setupCamera(width, height);
                    openCamera();
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }


            };
    private CameraDevice aCameraDevice;
    private CameraDevice.StateCallback aCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            aCameraDevice = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            aCameraDevice = null;

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            aCameraDevice = null;
        }
    };

    private CaptureRequest aPreviewCaptureRequest;
    private CaptureRequest.Builder aPreviewCaptureRequestBuilder;
    private CameraCaptureSession aCameraCaptureSession;
    private CameraCaptureSession.CaptureCallback aSessionCaptureCallback = new CameraCaptureSession.CaptureCallback()
    {
        private void process(CaptureResult result){
            switch (aState){
                case STATE_PREVIEW:
                    break;
                case STATE_WAIT_LOCK:
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if(afState == CaptureRequest.CONTROL_AF_STATE_FOCUSED_LOCKED){
                        try {
                            unlockFocus();
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "focus locked", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber)
        {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

            process(result);
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);

            Toast.makeText(getApplicationContext(), "focus unlocked", Toast.LENGTH_SHORT).show();
        }
    };

    private HandlerThread aBackgroundThread;
    private android.os.Handler aBackgroundHandler;
    private static File aImageFile;
    private ImageReader aImageReader;
    private final ImageReader.OnImageAvailableListener aOnImgageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader)
        {
            aBackgroundHandler.post(new ImageKeeper(reader.acquireNextImage()));
        }
    };

    private static class ImageKeeper implements Runnable
    {
        private final Image aImage;

        private ImageKeeper(Image image)
        {
            aImage = image;
        }

        @Override
        public void run()
        {
            ByteBuffer byteBuffer = aImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);


            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(aImageFile);
                fileOutputStream.write(bytes);
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                aImage.close();
                if(fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        button = (Button) findViewById(R.id.gallery);
        button2 = (Button) findViewById(R.id.exit);
        button3 = (Button) findViewById(R.id.TakePic);

/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
*/

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitPage();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        aTextureView = (TextureView) findViewById(R.id.ViewFinder);

    }

    private void exitPage() {

        finish();
    }

/*
    private void openGallery() {

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
*/
    @Override
    public void onResume() {
        super.onResume();

        if (aTextureView.isAvailable()) {
        } else {
            aTextureView.setSurfaceTextureListener(ViewFinderTextureListener);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    */

    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                Size largestImageSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new Comparator<Size>()
                        {
                            @Override
                            public int compare(Size lhs, Size rhs)
                            {
                                return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                            }
                        });
                aImageReader = ImageReader.newInstance(largestImageSize.getWidth(), largestImageSize.getHeight(), ImageFormat.JPEG, 1);
                aImageReader.setOnImageAvailableListener(aOnImgageAvailableListener, aBackgroundHandler);

                aPreviewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                aCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }



    private Size getPreferredPreviewSize(Size[] mapSizes, int width, int height) {
        List<Size> collectorSizes = new ArrayList<>();
        for (Size option : mapSizes) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    collectorSizes.add(option);
                }
            }

        }
        if (collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return mapSizes[0];
    }

    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraManager.openCamera(aCameraId, aCameraDeviceStateCallback, null);
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void takePic(View view)
    {
        try
        {
            aImageFile = createImageFile();
        }
        catch (IOException e)
            {
             e.printStackTrace();
            }
        }

        try
        {
            lockFocus();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void lockFocus() throws CameraAccessException {
        aState = STATE_WAIT_LOCK;

        aPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);

        aCameraCaptureSession.capture(aPreviewCaptureRequestBuilder.build(), aSessionCaptureCallback, aBackgroundHandler);

    }

    private void unlockFocus() throws CameraAccessException {
        aState = STATE_PREVIEW;

        aPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_CANCEL);

        aCameraCaptureSession.capture(aPreviewCaptureRequestBuilder.build(), aSessionCaptureCallback, aBackgroundHandler);

    }

}
