package com.example.pravaah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
// This is the main screen of entire application. Contains services cards like finance, todo, healthtrack, vault, contactsbook, wishlist
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView financeCard = findViewById(R.id.card1);
        CardView todoCard = findViewById(R.id.card2);
        CardView healthCard = findViewById(R.id.card3);
        CardView vaultCard = findViewById(R.id.card4);
        CardView contactsCard = findViewById(R.id.card5);
        CardView wishlistCard = findViewById(R.id.card6);

        financeCard.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainActivity.this, com.example.pravaah.services.splashes.FinanceSplashScreen.class);
            startActivity(intent);
        });

        todoCard.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainActivity.this, com.example.pravaah.services.splashes.TodoSplashScreen.class);
            startActivity(intent);
        });

        healthCard.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainActivity.this, com.example.pravaah.services.splashes.HealthtrackSplashScreen.class);
            startActivity(intent);
        });

        vaultCard.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainActivity.this, com.example.pravaah.services.splashes.VaultSplashScreen.class);
            startActivity(intent);
        });

        contactsCard.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainActivity.this, com.example.pravaah.services.splashes.ContactsbookSplashScreen.class);
            startActivity(intent);
        });

        wishlistCard.setOnClickListener((View view) -> {
            Intent intent = new Intent(MainActivity.this, com.example.pravaah.services.splashes.WishlistSplashScreen.class);
            startActivity(intent);
        });
    }
}