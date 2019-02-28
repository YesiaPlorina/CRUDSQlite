package com.yesia.crudsqlite;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.yesia.crudsqlite.adapter.DataDiriAdapter;
import com.yesia.crudsqlite.db.DataDiriHelper;
import com.yesia.crudsqlite.detail.DetailActivity;
import com.yesia.crudsqlite.model.DataDiri;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yesia.crudsqlite.detail.DetailActivity.EXTRA_POSITION;
import static com.yesia.crudsqlite.detail.DetailActivity.REQUEST_UPDATE;
import static com.yesia.crudsqlite.detail.DetailActivity.RESULT_DELETE;
import static com.yesia.crudsqlite.detail.DetailActivity.RESULT_UPDATE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    // deklarasi

    private LinkedList<DataDiri> list;
    private DataDiriAdapter adapter;
    private DataDiriHelper biodataHelper;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);


        list = new LinkedList<>();
        adapter = new DataDiriAdapter(this);
        adapter.setListDataDiri(list);

        // hubungkan dengan adapter
        recyclerview.setAdapter(adapter);

        // mengakses dan membuka database SQLite dengan membuat instance
        biodataHelper = new DataDiriHelper(this);
        biodataHelper.open();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                startActivityForResult(intent, DetailActivity.REQUEST_ADD);
            }
        });

        new LoadBiodataAsyntask().execute();
    }

    // method ini untuk meload data dari table dan nampilin ke dalam list
    private class LoadBiodataAsyntask extends AsyncTask<Void, Void, ArrayList<DataDiri>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (list.size() > 0) {
                list.clear();
            }
        }


        // secara asynchornous
        @Override
        protected ArrayList<DataDiri> doInBackground(Void... voids) {
            return biodataHelper.query();
        }


        // jika method doIn setelah dijalankan hasil nya akan dikirim kesini
        @Override
        protected void onPostExecute(ArrayList<DataDiri> biodata) {
            super.onPostExecute(biodata);


            list.addAll(biodata);
            adapter.setListDataDiri(list);
            adapter.notifyDataSetChanged();

            // dan menampilkan bahwa proses AsyncTask telah selesai
            if (list.size() == 0) {
                showSnackBar("Data Belum Ada");
            }
        }
    }

    // method untuk menampilkan notif pesan
    private void showSnackBar(String pesan) {
        Snackbar.make(recyclerview, pesan, Snackbar.LENGTH_LONG).show();
    }

    // class ini adalah class untuk menerima data yang dikirim dari
    // BiodataActivity berdasarkan requestCode dan resultCode nya
    // nilai hasil balik
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // nilai hasil balik jika ada penambahan
        if (requestCode == DetailActivity.REQUEST_ADD) {
            if (resultCode == DetailActivity.RESULT_ADD) {
                new LoadBiodataAsyntask().execute();
                recyclerview.getLayoutManager().smoothScrollToPosition(recyclerview,
                        new RecyclerView.State(), 0);
                showSnackBar("Biodata Berhasil Ditambahkan");
            }

            // nilai hasil balik jika ada perubahan
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_UPDATE) {
                new LoadBiodataAsyntask().execute();
                int position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0);
                recyclerview.getLayoutManager().smoothScrollToPosition(recyclerview,
                        new RecyclerView.State(), position);
                showSnackBar("Biodata Berhasil Diupdate");

                // nilai hasil balik jika ada penghapusan
            } else if (resultCode == RESULT_DELETE) {
                int position = data.getIntExtra(EXTRA_POSITION, 0);
                list.remove(position);
                adapter.setListDataDiri(list);
                adapter.notifyDataSetChanged();
                showSnackBar("Biodata Berhasil Dihapus");
            }
        }
    }


    // nutup akses ke database
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (biodataHelper != null) {
            biodataHelper.close();
        }
    }


}
