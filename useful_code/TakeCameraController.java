package com.test.camera2demo;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 默认摄像头前置
 *
 */
public class TakeCameraController {

    private Context mContext;
    //默认camera后置
    private String mCameraId = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);//摄像头id
    private TextureView mTextureView;
    private Display mDisplay;
    private Size previewSize;
    private CameraDevice.StateCallback mStateCallback;
    private CameraDevice mCameraDevice;
    private Surface previewsurface;
    private ImageReader imageReader;
    private CaptureRequest.Builder previewRequestBuilder;
    private CaptureRequest previewRequest;
    private CameraCaptureSession cameraCaptureSession;

    private final SparseIntArray ORIENTATIONS = new SparseIntArray();

    private TakeCameraController() {
    }

    private static class ClassHolder {
        static TakeCameraController mInstance = new TakeCameraController();
    }

    public static TakeCameraController getInstance() {
        return TakeCameraController.ClassHolder.mInstance;
    }


    public void initCamera(Context context, TextureView textureView, Display display) {
        this.mContext = context;
        this.mTextureView = textureView;
        this.mDisplay = display;
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            setTextureViewListener();
        }
    }

    private void openCamera() {
        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        //打开相机，第一个参数指示打开哪个摄像头，
        // 第二个参数stateCallback为相机的状态回调接口，
        // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
        mStateCallback = new CameraDevice.StateCallback() {

            @Override
            public void onOpened(@NonNull CameraDevice cameraDevices) {
                mCameraDevice = cameraDevices;
                startPreview();//开始预览
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int i) {
            }
        };
        if (ActivityCompat.checkSelfPermission(App.getInstance().getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            cameraManager.openCamera(mCameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //开始预览
    private void startPreview() {
        //创建imageReager,设置监听
        configImageReader();
        //设置TextureView的缓冲区大小并且创建 Surface 对象
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        previewsurface = new Surface(surfaceTexture);
        //发出预览请求
        try {
            previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        //设置预览的显示界面
        previewRequestBuilder.addTarget(previewsurface);
        previewRequest = previewRequestBuilder.build();//请求
        //创建相机捕获会话，
        // 第一个参数是捕获数据的输出Surface列表，
        // 第二个参数用于监听 Session 状态的CameraCaptureSession.StateCallback对象，
        // （是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，）
        // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行，
        // （用于执行 CameraCaptureSession.StateCallback 的 Handler 对象，可以是异步线程的 Handler，也可以是主线程的 Handler）
        try {
            CameraCaptureSession.StateCallback sessionStateCallback = new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession captureSession) {
                    cameraCaptureSession = captureSession;//会话
                    repeatPreview();//重复预览
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            };
            mCameraDevice.createCaptureSession(Arrays.asList(previewsurface, imageReader.getSurface()), sessionStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //重复预览
    private void repeatPreview() {
        CameraCaptureSession.CaptureCallback previewCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        };
        try {
            cameraCaptureSession.setRepeatingRequest(previewRequest, previewCaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void configImageReader() {
        //前三个参数分别是需要的尺寸和格式，最后一个参数代表每次最多获取几帧数据, Image 对象池的大小
        imageReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(), ImageFormat.JPEG, 1);
        //监听ImageReader的事件，当有图像流数据可用时会回调onImageAvailable方法，它的参数就是预览帧数据，可以对这帧数据进行处理
        //当有图像数据生成的时候，ImageReader 会通过通过 ImageReader.OnImageAvailableListener.onImageAvailable() 方法通知我们
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = imageReader.acquireLatestImage();//获取生成的图片
                // if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=)
                new Thread(new ImageSaver(image)).start();// 保存图片
            }
        }, null);
    }

    /**
     * 将JPG保存到指定的文件中。
     */
    private static class ImageSaver implements Runnable {

        /**
         * JPEG图像
         */
        private final Image mImage;
        /**
         * 保存图像的文件
         */
        private final File mFile;

        public ImageSaver(Image image) {
            mImage = image;
            mFile = new File(App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    System.currentTimeMillis() + ".jpg");
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                if (!mFile.exists()) {
                    mFile.createNewFile();
                }
                Log.i("wislieZhu","File path="+mFile.getPath());
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    //为textureView设置一个监听，当onSurfaceTextureAvailable即Texture可用的时候
    // 获取预览尺寸checkCamera(),
    // 配置预览尺寸configureTransform(),
    // 打开相机
    private void setTextureViewListener() {
        //textureView设置一个监听
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            //在textureview可用的时候
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
                checkCamera(width, height);//获取预览尺寸
                configureTransform(width, height);//把矩阵添加到TextureView，配置预览尺寸
                openCamera();//打开相机
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

            }
        });
    }

    private void checkCamera(int width, int height) {
        //获取摄像头的管理者
        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            //遍历摄像头
            for (String id : cameraManager.getCameraIdList()) {
                // 默认打开后置摄像头 - 忽略前置摄像头
                if (!mCameraId.equals(id)) continue;
                // CameraCharacteristics 是一个只读的相机信息提供者，
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                //通过 CameraCharacteristics 获取该设备支持的所有预览尺寸
                // （先通过 SCALER_STREAM_CONFIGURATION_MAP 获取 StreamConfigurationMap 对象，
                // 然后通过 StreamConfigurationMap.getOutputSizes() 方法获取尺寸列表
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                //Camera2 则是把尺寸信息设置给 Surface，例如接收预览画面的 SurfaceTexture，或者是接收拍照图片的 ImageReader，
                // 相机在输出图像数据的时候会根据 Surface 配置的 Buffer 大小输出对应尺寸的画面。
                if(characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){ //前置
                    frontOrientation();
                }else if(characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK){ //后置
                    backOrientation();
                }
                //对ORIENTATION赋值,默认为后置，因此旋转90
                previewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                //获取TextureView的尺寸配置
                break;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 选择sizeMap中大于并且最接近width和height的size
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mCameraId || null == previewSize) {
            return;
        }
        //角度
        int rotation = mDisplay.getRotation();

        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewSize.getHeight(),
                    (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }


    //前置拍摄时，照片旋转270
    private void frontOrientation() {
        //前置时，照片旋转270
        ORIENTATIONS.append(Surface.ROTATION_0, 270);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 90);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    //后置拍摄时，照片旋转90
    private void backOrientation() {
        //后置时，照片旋转90
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * 拍照
     */
    public void capture() {
        try {
            //首先我们创建请求拍照的CaptureRequest
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

            //获取屏幕方向
            int rotation = mDisplay.getRotation();
            //一个 CaptureRequest 除了需要配置很多参数之外，还要求至少配置一个 Surface（任何相机操作的本质都是为了捕获图像），
            captureBuilder.addTarget(previewsurface);
            captureBuilder.addTarget(imageReader.getSurface());

            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

            int val = ORIENTATIONS.get(rotation);
            Log.i("wislieZhu","val="+val +" mCameraId="+mCameraId);
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            cameraCaptureSession.stopRepeating();
            CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
//                    Toast.makeText(MainActivity.this, "图片已保存", Toast.LENGTH_SHORT).show();
                    repeatPreview();
                }
            };
            cameraCaptureSession.capture(captureBuilder.build(), captureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void switchCamera() {
        //获取摄像头的管理者
        CameraManager cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                previewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), mTextureView.getWidth(), mTextureView.getHeight());
                if (mCameraId.equals(String.valueOf(CameraCharacteristics.LENS_FACING_BACK))
                        && characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    mCameraId = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
                    mCameraDevice.close();
                    backOrientation();
                    openCamera();
                    break;
                } else if (mCameraId.equals(String.valueOf(CameraCharacteristics.LENS_FACING_FRONT))
                        && characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    mCameraId = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);
                    mCameraDevice.close();
                    frontOrientation();
                    openCamera();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
