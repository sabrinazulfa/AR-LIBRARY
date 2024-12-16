package com.airlibrary.utilities;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.airlibrary.models.Document;
import com.airlibrary.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    // Mendapatkan pengguna saat ini
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Menyimpan dokumen baru
    public Task<DocumentReference> saveDocument(Document document) {
        return firestore.collection("documents")
            .add(document);
    }

    // Mengunggah file ke Firebase Storage
    public Task<Uri> uploadFile(Uri fileUri, String fileType) {
        StorageReference storageRef = storage.getReference();
        String fileName = fileType + "/" + UUID.randomUUID().toString();
        StorageReference fileRef = storageRef.child(fileName);

        return fileRef.putFile(fileUri)
            .continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();
            });
    }

    // Menyimpan profil pengguna
    public Task<Void> saveUserProfile(User user) {
        return firestore.collection("users")
            .document(user.getId())
            .set(user);
    }

    // Mendapatkan profil pengguna
    public Task<DocumentReference> getUserProfile(String userId) {
        return firestore.collection("users")
            .document(userId)
            .get()
            .continueWith(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Berhasil mendapatkan profil pengguna");
                    return task.getResult();
                } else {
                    Log.e(TAG, "Gagal mendapatkan profil pengguna", task.getException());
                    return null;
                }
            });
    }

    // Logout
    public void logout() {
        mAuth.signOut();
    }
}