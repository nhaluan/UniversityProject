package com.example.group13;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btnAddImage;
    private ImageButton btnDeleteImage;
    private ImageView imgMain;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String msg ="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        ImageView[] imageViews = new ImageView[9]; // Tạo một mảng ImageView với 9 phần tử
        // Khởi tạo từng ImageView trong mảng
        imageViews[0] = findViewById(R.id.imageView1);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);
        imageViews[3] = findViewById(R.id.imageView4);
        imageViews[4] = findViewById(R.id.imageView5);
        imageViews[5] = findViewById(R.id.imageView6);
        imageViews[6] = findViewById(R.id.imageView7);
        imageViews[7] = findViewById(R.id.imageView8);
        imageViews[8] = findViewById(R.id.imageView9);

        //
        btnAddImage = findViewById(R.id.addImageButton);
        btnDeleteImage = findViewById(R.id.deleteButton);
        imgMain = findViewById(R.id.imageAdd);

        //
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        btnDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    imgMain.setImageURI(null);
                    selectedImageUri = null;
                }
            }
        });

        imgMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v , MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("" , "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imgMain);
                    v.startDrag(data , shadowBuilder , v , 0);
                    v.setVisibility(View.VISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });
        // Đặt OnDragListener cho mỗi ImageView trong mảng 3x3
        for (ImageView imageView : imageViews) {
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && imageView.getDrawable() != null) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);
                        v.startDrag(data, shadowBuilder, imageView, 0);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            imageView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_ENTERED:
                            // Khi hình ảnh được kéo vào ô, thay đổi màu của ô thành xanh lá
                            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.green));
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            // Khi hình ảnh rời khỏi ô, thay đổi màu của ô trở lại màu xanh dương
                            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.blue));
                            break;
                        case DragEvent.ACTION_DROP:
                            // Khi hình ảnh được thả vào ô, cập nhật hình ảnh của ô với hình ảnh đã được kéo
                            ImageView droppedImageView = (ImageView) event.getLocalState();
                            if (v != droppedImageView) {
                                ((ImageView)v).setImageDrawable(droppedImageView.getDrawable());

                                // Xóa hình ảnh từ ImageView ban đầu
                                droppedImageView.setImageDrawable(null);

                                // Thay đổi màu nền của ô thành xanh lá
                                v.setBackgroundColor(Color.GREEN);
                            }
                        case DragEvent.ACTION_DRAG_ENDED:
                            // Khi kết thúc việc kéo, kiểm tra xem hình ảnh có được thả vào một ImageView mới không
                            if (!event.getResult()) {
                                // Nếu không, đặt lại hình ảnh cho ImageView ban đầu
                                ImageView originalImageView = (ImageView) event.getLocalState();
                                originalImageView.setVisibility(View.VISIBLE);
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });


        }
        for (final ImageView imageView : imageViews) {
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN && imageView.getDrawable() != null) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);
                        v.startDrag(data, shadowBuilder, v, 0);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }


        btnDeleteImage.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        // Khi hình ảnh được thả vào ImageButton, xóa hình ảnh từ ImageView ban đầu
                        ImageView droppedImageView = (ImageView) event.getLocalState();
                        droppedImageView.setImageDrawable(null);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Lưu đường dẫn của hình ảnh đã chọn
            selectedImageUri = data.getData();

            // Hiển thị hình ảnh đã chọn
            imgMain.setImageURI(selectedImageUri);
        }
    }

}