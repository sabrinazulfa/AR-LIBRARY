package com.airlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.airlibrary.models.Document;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView documentListView;
    private DocumentAdapter documentAdapter;
    private List<Document> documentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Inisialisasi ListView dan Adapter
        documentListView = findViewById(R.id.documentListView);
        documentList = new ArrayList<>();
        documentAdapter = new DocumentAdapter(this, documentList);
        documentListView.setAdapter(documentAdapter);

        // Memuat dokumen dari Firestore
        loadDocumentsFromFirestore();

        // Menangani klik item dokumen
        documentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Document selectedDocument = documentList.get(position);
                
                // Intent untuk melihat dokumen
                Intent viewIntent = new Intent(MainActivity.this, DocumentViewActivity.class);
                viewIntent.putExtra("document", selectedDocument);
                startActivity(viewIntent);
            }
        });
    }

    private void loadDocumentsFromFirestore() {
        db.collection("documents")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    documentList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Document doc = document.toObject(Document.class);
                        documentList.add(doc);
                    }
                    documentAdapter.notifyDataSetChanged();
                } else {
                    // Tangani error loading dokumen
                    // Misalnya, tampilkan pesan error
                }
            });
    }
}