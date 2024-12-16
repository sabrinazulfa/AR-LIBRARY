package com.example.air_library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.air_library.models.Document;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.UUID;

public class UploadDocumentActivity extends AppCompatActivity {
    private static final int PICK_PDF_REQUEST = 1;

    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;
    private Button selectPdfButton;
    private Button uploadButton;
    private TextView selectedFileTextView;
    private ProgressBar uploadProgressBar;

    private Uri selectedPdfUri;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);

        // Inisialisasi Firebase
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        // Inisialisasi View
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        selectPdfButton = findViewById(R.id.selectPdfButton);
        uploadButton = findViewById(R.id.uploadButton);
        selectedFileTextView = findViewById(R.id.selectedFileTextView);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);

        // Listener untuk memilih PDF
        selectPdfButton.setOnClickListener(v -> openFilePicker());

        // Listener untuk upload dokumen
        uploadButton.setOnClickListener(v -> validateAndUploadDocument());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Pilih File PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedPdfUri = data.getData();
            
            // Tampilkan nama file yang dipilih
            String fileName = getFileNameFromUri(selectedPdfUri);
            selectedFileTextView.setText(fileName);
            selectedFileTextView.setVisibility(View.VISIBLE);
        }
    }

    private void validateAndUploadDocument() {
        // Validasi input
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Cek validasi
        if (title.isEmpty()) {
            titleEditText.setError("Judul harus diisi");
            return;
        }

        if (selectedPdfUri == null) {
            Toast.makeText(this, "Pilih file PDF terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aktifkan progress bar
        uploadProgressBar.setVisibility(View.VISIBLE);
        uploadButton.setEnabled(false);

        // Proses upload file ke Firebase Storage
        StorageReference storageRef = storage.getReference();
        String fileName = "documents/" + UUID.randomUUID().toString() + ".pdf";
        StorageReference documentRef = storageRef.child(fileName);

        // Upload file
        documentRef.putFile(selectedPdfUri)
            .addOnProgressListener(snapshot -> {
                // Update progress
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                uploadProgressBar.setProgress((int) progress);
            })
            .addOnSuccessListener(taskSnapshot -> {
                // Dapatkan URL download
                documentRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Buat objek dokumen
                    Document document = new Document();
                    document.setTitle(title);
                    document.setDescription(description);
                    document.setFileUrl(uri.toString());
                    document.setUploadDate(new Date());
                    document.setFileType("PDF");
                    document.setFileSize(taskSnapshot.getBytesTransferred());
                    
                    // Tambahkan informasi pengunggah jika pengguna sudah login
                    if (auth.getCurrentUser() != null) {
                        document.setUploadedBy(auth.getCurrentUser().getEmail());
                    }

                    // Simpan metadata ke Firestore
                    firestore.collection("documents")
                        .add(document)
                        .addOnSuccessListener(documentReference -> {
                            // Berhasil
                            Toast.makeText(UploadDocumentActivity.this, 
                                "Dokumen berhasil diunggah", 
                                Toast.LENGTH_SHORT).show();
                            
                            // Reset UI
                            resetUploadForm();
                        })
                        .addOnFailureListener(e -> {
                            // Gagal menyimpan metadata
                            Toast.makeText(UploadDocumentActivity.this, 
                                "Gagal menyimpan metadata dokumen", 
                                Toast.LENGTH_SHORT).show();
                            
                            // Aktifkan ulang tombol
                            uploadButton.setEnabled(true);
                            uploadProgressBar.setVisibility(View.GONE);
                        });
                });
            })
            .addOnFailureListener(e -> {
                // Gagal upload
                Toast.makeText(this, "Gagal mengunggah dokumen", Toast.LENGTH_SHORT).show();
                
                // Aktifkan ulang tombol
                uploadButton.setEnabled(true);
                uploadProgressBar.setVisibility(View.GONE);
            });
    }

    private void resetUploadForm() {
        // Reset semua field
        titleEditText.setText("");
        descriptionEditText.setText("");
        selectedPdfUri = null;
        selectedFileTextView.setText("");
        selectedFileTextView.setVisibility(View.GONE);
        uploadProgressBar.setVisibility(View.GONE);
        uploadButton.setEnabled(true);
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}