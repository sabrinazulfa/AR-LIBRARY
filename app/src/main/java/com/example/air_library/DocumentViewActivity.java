package com.airlibrary;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airlibrary.models.Document;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DocumentViewActivity extends AppCompatActivity {
    private PDFView pdfView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView metadataTextView;
    private Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);

        // Inisialisasi View
        pdfView = findViewById(R.id.pdfView);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        metadataTextView = findViewById(R.id.metadataTextView);

        // Ambil dokumen dari Intent
        document = (Document) getIntent().getSerializableExtra("document");

        if (document != null) {
            // Set informasi dokumen
            titleTextView.setText(document.getTitle());
            descriptionTextView.setText(document.getDescription());

            // Format metadata
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
            String metadata = String.format(
                "Ukuran: %s\nTipe: %s\nDiunggah: %s", 
                formatFileSize(document.getFileSize()), 
                document.getFileType(), 
                sdf.format(document.getUploadDate())
            );
            metadataTextView.setText(metadata);

            // Unduh dan tampilkan PDF dari Firebase Storage
            loadPdfFromFirebase(document.getFileUrl());
        } else {
            Toast.makeText(this, "Dokumen tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadPdfFromFirebase(String fileUrl) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(fileUrl);

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            // Tampilkan PDF
            pdfView.fromBytes(bytes)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();
        }).addOnFailureListener(exception -> {
            // Tangani kesalahan
            Toast.makeText(this, "Gagal memuat dokumen", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Tambahkan menu download
        getMenuInflater().inflate(R.menu.menu_document_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            downloadDocument();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadDocument() {
        // Implementasi download dokumen
        Toast.makeText(this, "Fitur download masih dalam pengembangan", Toast.LENGTH_SHORT).show();
    }

    // Utility method untuk format ukuran file
    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        
        return String.format(Locale.getDefault(), "%.1f %s", 
            size / Math.pow(1024, digitGroups), 
            units[digitGroups]
        );
    }
}