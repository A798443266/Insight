package com.luo.a10.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luo.a10.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.iv)
    ImageView iv;

    private boolean ISBACK;//是否已经拍了正面身份证
    private boolean BOTH;//身份证正反都拍了


    //拍摄类型-身份证正面
    public final static int TYPE_IDCARD_FRONT = 1;
    //拍摄类型-身份证反面
    public final static int TYPE_IDCARD_BACK = 2;
    //拍摄类型-竖版营业执照
    public final static int TYPE_COMPANY_PORTRAIT = 3;
    //拍摄类型-横版营业执照
    public final static int TYPE_COMPANY_LANDSCAPE = 4;
    public final static int REQUEST_CODE = 0X13;
    public final static int RESULT_CODE = 0X14;

    private CameraPreview cameraPreview;
    private View containerView;
    private ImageView cropView;
    private ImageView flashImageView;
    private View optionView;
    private View resultView;
    private int type;

    private String[] titles = {"身份证", "营业执照", "户口本", "驾驶证"};
    private float screenMinSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", 0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }

        initData();
    }

    private void initData() {

        cameraPreview = (CameraPreview) findViewById(R.id.camera_surface);
        //获取屏幕最小边，设置为cameraPreview较窄的一边
        screenMinSize = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        //根据screenMinSize，计算出cameraPreview的较宽的一边，长宽比为标准的16:9
        float maxSize = screenMinSize / 9.0f * 16.0f;
        RelativeLayout.LayoutParams layoutParams;
        layoutParams = new RelativeLayout.LayoutParams((int) screenMinSize, (int) maxSize);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cameraPreview.setLayoutParams(layoutParams);
        containerView = findViewById(R.id.camera_crop_container);
        cropView = (ImageView) findViewById(R.id.camera_crop);


        //第一次进来tablayout还没有选择，要先初始化一下
        cropView.setVisibility(View.VISIBLE);
        float width = (int) (screenMinSize * 0.75);
        float height = (int) (width * 75.0f / 47.0f);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
        LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
        containerView.setLayoutParams(containerParams);
        cropView.setLayoutParams(cropParams);
        cropView.setImageResource(R.mipmap.camera_idcard_front);

        flashImageView = (ImageView) findViewById(R.id.camera_flash);
        optionView = findViewById(R.id.camera_option);
        resultView = findViewById(R.id.camera_result);
        cameraPreview.setOnClickListener(this);
        findViewById(R.id.camera_close).setOnClickListener(this);
        findViewById(R.id.camera_take).setOnClickListener(this);
        flashImageView.setOnClickListener(this);
        findViewById(R.id.camera_result_ok).setOnClickListener(this);
        findViewById(R.id.camera_result_cancel).setOnClickListener(this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                type = tab.getPosition();
                iv.setVisibility(View.GONE);
                switch (tab.getPosition()) {
                    case 0:
                        cropView.setVisibility(View.VISIBLE);
                        float width = (int) (screenMinSize * 0.75);
                        float height = (int) (width * 75.0f / 47.0f);
                        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
                        LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
                        containerView.setLayoutParams(containerParams);
                        cropView.setLayoutParams(cropParams);
                        cropView.setImageResource(R.mipmap.camera_idcard_front);
                        break;
                    case 1:
                        ISBACK = false;
                        cropView.setVisibility(View.VISIBLE);
                        float width1 = (int) (screenMinSize * 0.8);
                        float height1 = (int) (width1 * 43.0f / 30.0f);
                        LinearLayout.LayoutParams containerParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height1);
                        LinearLayout.LayoutParams cropParams1 = new LinearLayout.LayoutParams((int) width1, (int) height1);
                        containerView.setLayoutParams(containerParams1);
                        cropView.setLayoutParams(cropParams1);
                        cropView.setImageResource(R.mipmap.camera_company);
                        break;

                    case 2:
                        ISBACK = false;
                        cropView.setVisibility(View.VISIBLE);
                        float width3 = (int) (screenMinSize * 0.75);
                        float height3 = (int) (width3 * 75.0f / 47.0f);
                        LinearLayout.LayoutParams containerParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height3);
                        LinearLayout.LayoutParams cropParams3 = new LinearLayout.LayoutParams((int) width3, (int) height3);
                        containerView.setLayoutParams(containerParams3);
                        cropView.setLayoutParams(cropParams3);
                        cropView.setImageResource(R.mipmap.camera_idcard_back);
                        break;
                    case 3:
                        ISBACK = false;
                        cropView.setVisibility(View.VISIBLE);
                        float width4 = (int) (screenMinSize * 0.8);
                        float height4 = (int) (width4 * 43.0f / 30.0f);
                        LinearLayout.LayoutParams containerParams4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height4);
                        LinearLayout.LayoutParams cropParams4 = new LinearLayout.LayoutParams((int) width4, (int) height4);
                        containerView.setLayoutParams(containerParams4);
                        cropView.setLayoutParams(cropParams4);
                        cropView.setImageResource(R.mipmap.camera_company);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_surface) {
            cameraPreview.focus();
        } else if (id == R.id.camera_close) {
            finish();
        } else if (id == R.id.camera_take) {
            takePhoto();

        } else if (id == R.id.camera_flash) {
            boolean isFlashOn = cameraPreview.switchFlashLight();
            flashImageView.setImageResource(isFlashOn ? R.mipmap.camera_flash_on : R.mipmap.camera_flash_off);
        } else if (id == R.id.camera_result_ok) {

            if (type == 0 && ISBACK && !BOTH) {//拍摄下一张背面的身份证
                cropView.setVisibility(View.VISIBLE);
                float width3 = (int) (screenMinSize * 0.75);
                float height3 = (int) (width3 * 75.0f / 47.0f);
                LinearLayout.LayoutParams containerParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height3);
                LinearLayout.LayoutParams cropParams3 = new LinearLayout.LayoutParams((int) width3, (int) height3);
                containerView.setLayoutParams(containerParams3);
                cropView.setLayoutParams(cropParams3);
                cropView.setImageResource(R.mipmap.camera_idcard_back);

                iv.setVisibility(View.VISIBLE);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(getCropFile().get(0).getPath());
                    Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes=baos.toByteArray();
                    Glide.with(this).load(bytes).error(R.drawable.city1).into(iv);

                } catch (FileNotFoundException e) {

                }

                ll.setVisibility(View.VISIBLE);
                cameraPreview.setEnabled(true);
                resultView.setVisibility(View.GONE);
                cameraPreview.startPreview();
                Toast.makeText(this, "请拍摄身份证反面", Toast.LENGTH_SHORT).show();
            } else { //其他的只要拍一张就可以返回了
                goBack();
            }

        } else if (id == R.id.camera_result_cancel) {
            ll.setVisibility(View.VISIBLE);
            cameraPreview.setEnabled(true);
            resultView.setVisibility(View.GONE);
            cameraPreview.startPreview();
        }
    }


    private void takePhoto() {
        ll.setVisibility(View.GONE);//拍照消失
        cameraPreview.setEnabled(false);
        cameraPreview.takePhoto(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                camera.stopPreview();
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File originalFile;
                            if (type == 0 && ISBACK) {//已经拍了身份证正面
                                originalFile = getOriginalFile().get(1);
                            } else {
                                originalFile = getOriginalFile().get(0);
                            }

                            FileOutputStream originalFileOutputStream = new FileOutputStream(originalFile);
                            originalFileOutputStream.write(data);
                            originalFileOutputStream.close();

                            Bitmap bitmap = BitmapFactory.decodeFile(originalFile.getPath());

                            //计算裁剪位置
                            float left, top, right, bottom;

                            left = (float) cropView.getLeft() / (float) cameraPreview.getWidth();
                            top = ((float) containerView.getTop() - (float) cameraPreview.getTop()) / (float) cameraPreview.getHeight();
                            right = (float) cropView.getRight() / (float) cameraPreview.getWidth();
                            bottom = (float) containerView.getBottom() / (float) cameraPreview.getHeight();

                            //裁剪及保存到文件
                            Bitmap cropBitmap = Bitmap.createBitmap(bitmap,
                                    (int) (left * (float) bitmap.getWidth()),
                                    (int) (top * (float) bitmap.getHeight()),
                                    (int) ((right - left) * (float) bitmap.getWidth()),
                                    (int) ((bottom - top) * (float) bitmap.getHeight()));

                            File cropFile;
                            if (type == 0 && ISBACK) {//已经拍了身份证正面
                                cropFile = getCropFile().get(1);
                            } else {
                                cropFile = getCropFile().get(0);
                            }

                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cropFile));
                            cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            bos.flush();
                            bos.close();
                            if (type == 0 && ISBACK) {//两张身份证都拍完
                                BOTH = true;
                            }

                            if (type == 0 && !ISBACK) //拍完正面
                                ISBACK = true;


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resultView.setVisibility(View.VISIBLE);
                                }
                            });
                            return;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll.setVisibility(View.VISIBLE);
                                cameraPreview.setEnabled(true);
                            }
                        });
                    }
                }).start();

            }
        });
    }

    /**
     * @return 拍摄图片原始文件
     */
    private List<File> getOriginalFile() {
        List<File> files = new ArrayList<>();
        if (type == 0) {//如果是拍身份证，要拍正反面，要两个文件
            files.add(new File(getExternalCacheDir(), "picture.jpg"));
            files.add(new File(getExternalCacheDir(), "picture1.jpg"));
        } else {
            files.add(new File(getExternalCacheDir(), "picture.jpg"));
        }
        return files;
    }

    /**
     * @return 拍摄图片裁剪文件
     */
    private List<File> getCropFile() {
        List<File> files = new ArrayList<>();
        if (type == 0) {//如果是拍身份证，要拍正反面，要两个文件
            files.add(new File(getExternalCacheDir(), "pictureCrop.jpg"));
            files.add(new File(getExternalCacheDir(), "pictureCrop1.jpg"));
        } else {
            files.add(new File(getExternalCacheDir(), "pictureCrop.jpg"));
        }
        return files;
    }

    /**
     * 点击对勾，使用拍照结果，返回对应图片路径
     */
    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        if (type == 0) {
            intent.putExtra("path", getCropFile().get(0).getPath());
            intent.putExtra("path1", getCropFile().get(1).getPath());
        } else {
            intent.putExtra("path", getCropFile().get(0).getPath());
        }
        setResult(RESULT_CODE, intent);
        finish();
    }

    public static void openCertificateCamera(Activity activity, int type) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * @return 结果文件路径
     */
    public static String getResult(Intent data) {
        if (data != null) {
            return data.getStringExtra("path");
        }
        return "";
    }

}
