package com.airlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airlibrary.models.Document;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DocumentListActivity extends AppCompatActivity {
    private ListView documentListView;
    private DocumentAdapter documentAdapter;
    private List<Document> documentList;
    private TextView emptyTextView;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);

        // Inisialisasi Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Inisialisasi View
        documentListView = findViewById(R.id.documentListView);
        emptyTextView = findViewById(R.id.emptyTextView);

        // Persiapan list dokumen
        documentList = new ArrayList<>();
        documentAdapter = new DocumentAdapter(this, documentList);
        documentListView.setAdapter(documentAdapter);

        // Set empty view
        documentListView.setEmptyView(emptyTextView);

        // Listener untuk klik item dokumen
        documentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Document selectedDocument = documentList.get(position);
                
                // Pindah ke DocumentViewActivity
                Intent viewIntent = new Intent(DocumentListActivity.this, DocumentViewActivity.class);
                viewIntent.putExtra("document", selectedDocument);
                startActivity(viewIntent);
            }
        });

        // Muat dokumen dari Firestore
        loadDocuments();
    }

    private void loadDocuments() {
        firestore.collection("documents")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    documentList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Document doc = document.toObject(Document.class);
                        documentList.add(doc);
                    }
                    documentAdapter.notifyDataSetChanged();

                    // Tampilkan pesan jika tidak ada dokumen
                    if (documentList.isEmpty()) {
                        emptyTextView.setText("Tidak ada dokumen tersedia");
                    }
                } else {
                    // Tangani error
                    emptyTextView.setText("Gagal memuat dokumen");
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Tambahkan menu upload dokumen
        getMenuInflater().inflate(R.menu.menu_document_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Tangani pilihan menu
        if (item.getItemId() == R.id.action_upload) {
            // Buka aktivitas unggah dokumen
            Intent uploadIntent = new Intent(this, UploadDocumentActivity.class);
            startActivity(uploadIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang dokumen saat aktivitas dilanjutkan
        loadDocuments();
    }
}