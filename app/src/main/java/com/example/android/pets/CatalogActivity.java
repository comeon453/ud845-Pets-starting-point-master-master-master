/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.CursorAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.android.pets.Data.PetContract;
import com.example.android.pets.Data.PetDbHelper;

import static com.example.android.pets.Data.PetContract.PetEntry.CONTENT_URI;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int PET_LOADER = 0;
    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.pet_list_view);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        //Setup an adapter to create a list of item for each row of pet data in the cursor.
        //There is no pet data yet (until the loader finished) so pass in null for the cursor.
        mCursorAdapter = new PetCursorAdapter(this, null, 0);
        petListView.setAdapter(mCursorAdapter);

        //setup onclick listener for pet list
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);


                //for the content URI that represents the pet that was clicked on by appending id
                //to the content URI
                Uri selectedPetUri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI, id);

                //Set the URI on the data field of the intent
                intent.setData(selectedPetUri);


                startActivity(intent);
            }
        });

        //kick off the loader
        getSupportLoaderManager().initLoader(PET_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }


    protected void onStart() {
        super.onStart();
        //  displayDatabaseInfo();
    }

    /*Inserts a dummy pet data*/
    private void insertDummyPet() {


        /*Create a set of content values */
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);
        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.

        Uri newUri = getContentResolver().insert(CONTENT_URI, values);


    }

    private void deleteAllPets() {
        getContentResolver().delete(CONTENT_URI, null, null);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyPet();
                //  displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                // displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


/*    private void displayDatabaseInfo() {

       String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED
        };

        String selection = PetContract.PetEntry.COLUMN_PET_GENDER + " = ?";
        String[] selectionArgs = { "1" };

       *//* Cursor cursor = db.query(
                PetContract.PetEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );*//*
       Cursor cursor =  getContentResolver().query(CONTENT_URI , projection, null, null,null);
             try {


            PetCursorAdapter mPetCursorAdapter = new PetCursorAdapter(this, cursor, 0);
            ListView displayView = (ListView) findViewById(R.id.pet_list_view);
            displayView.setAdapter(mPetCursorAdapter);
             }
             finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
           // cursor.close();
        }
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED
        };
        // create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, PetContract.PetEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mCursorAdapter.swapCursor(null);
    }
}
