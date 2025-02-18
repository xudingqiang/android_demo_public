package com.bella.android_demo_public.activity;

import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.bella.android_demo_public.R;
import com.bella.android_demo_public.utils.LogTool;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PdfTestActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    private static final int REQUEST_CODE = 1;
//    private PdfiumCore pdfiumCore;
//    private PdfDocument pdfDocument;
    private int currentPage = 0;
    private ImageView imageView;
    PDFView pdfView;
    public static final String SAMPLE_FILE = "bb.pdf";
    Integer pageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_test);

        imageView = findViewById(R.id.imageView);
        pdfView = findViewById(R.id.pdfView);
        Button buttonPrevious = findViewById(R.id.buttonPrevious);
        Button buttonNext = findViewById(R.id.buttonNext);

        buttonPrevious.setOnClickListener(v -> previousPage());
        buttonNext.setOnClickListener(v -> nextPage());

//        pdfiumCore = new PdfiumCore(this);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
//        } else {
//            loadPDF("/mnt/sdcard/aa.pdf");
//        }

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(currentPage)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogTool.i("onRequestPermissionsResult "+requestCode);
        if (requestCode == REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            loadPDF("/mnt/sdcard/aa.pdf");
        }
    }

//    private void loadPDF(String filePath) {
//        try {
//            File file = new File(filePath);
//            if(!file.exists()){
//                LogTool.e("pdffile is not exists........");
//            }else {
//                LogTool.i("pdffile is exists........");
//            }
//            ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//            pdfDocument = pdfiumCore.newDocument(fd);
//            int numPages = pdfiumCore.getPageCount(pdfDocument);
//            renderPage(0); // 渲染第一页
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void renderPage(int page) {
//        try {
//            pdfiumCore.openPage(pdfDocument, page);
//            int width = pdfiumCore.getPageWidthPoint(pdfDocument, page);
//            int height = pdfiumCore.getPageHeightPoint(pdfDocument, page);
//            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, page, 0, 0, width, height);
//            imageView.setImageBitmap(bitmap);
//            currentPage = page;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
    private void nextPage() {
//        if (currentPage < pdfiumCore.getPageCount(pdfDocument) - 1) {
//            renderPage(currentPage + 1);
            currentPage++;
            pdfView.jumpTo(currentPage);
//        }
    }

    private void previousPage() {
        if (currentPage > 0) {
//            renderPage(currentPage - 1);
            currentPage--;
            pdfView.jumpTo(currentPage);
        }
    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (pdfDocument != null) {
//            pdfiumCore.closeDocument(pdfDocument);
//        }
//    }


    @Override
    public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {

    }

    @Override
    public void loadComplete(int i) {

    }

    @Override
    public void onPageChanged(int i, int i1) {

    }

    @Override
    public void onPageError(int i, Throwable throwable) {

    }
}