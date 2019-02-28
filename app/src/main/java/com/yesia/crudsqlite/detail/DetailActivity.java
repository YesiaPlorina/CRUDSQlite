package com.yesia.crudsqlite.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.yesia.crudsqlite.R;
import com.yesia.crudsqlite.db.DataDiriHelper;
import com.yesia.crudsqlite.model.DataDiri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.edt_nama)
    EditText edtNama;
    @BindView(R.id.edt_umur)
    EditText edtUmur;
    @BindView(R.id.edt_gender)
    EditText edtGender;
    @BindView(R.id.edt_weight)
    EditText edtWeight;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 300;

    private int position;
    private DataDiri biodata;
    private DataDiriHelper biodataHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        // akses dan buka database SQLite dengan membuat instance
        biodataHelper = new DataDiriHelper(this);
        biodataHelper.open();

        // nerima data dari class model
        biodata = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (biodata != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }

        String actionBarTitle = null;
        String btnTitle = null;

        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            edtNama.setText(biodata.getName());
            edtUmur.setText(biodata.getAge());
            edtGender.setText(biodata.getGender());
            edtWeight.setText(biodata.getWeight());

        } else {

            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setText(btnTitle);

    }

    //mengahcurkan akses database
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (biodataHelper != null) {
            biodataHelper.close();
        }
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {


        String nama = edtNama.getText().toString();
        String umur = edtUmur.getText().toString().trim();
        String gender = edtGender.getText().toString().trim();
        String weight = edtWeight.getText().toString().trim();
        String inputan = "Tidak Boleh Kosong";

        // inisialisai untuk inputan field
        boolean isempty = false;

        // cek inputan jika inputan kosong nampilin error
        if (TextUtils.isEmpty(nama)) {
            isempty = true;
            edtNama.setError(inputan);
        } else if (TextUtils.isEmpty(umur)) {
            isempty = true;
            edtUmur.setError(inputan);
        } else if (TextUtils.isEmpty(gender)) {
            isempty = true;
            edtGender.setError(inputan);
        } else if (TextUtils.isEmpty(weight)) {
            isempty = true;
            edtWeight.setError(inputan);
        }

        // jika tidak kosong
        if (!isempty) {
            DataDiri b = new DataDiri();
            b.setName(nama);
            b.setAge(umur);
            b.setGender(gender);
            b.setWeight(weight);

            Intent i = new Intent();

            // dieksekusi jika ada update data
            if (isEdit) {
                b.setDate(biodata.getDate());
                b.setId(biodata.getId());
                biodataHelper.update(b);

                // lalu dikirim melalui RESULT_UPDATE
                i.putExtra(EXTRA_POSITION, position);
                setResult(RESULT_UPDATE, i);
                finish();

                // dieksekusi pada saat insert data
            } else {
                b.setDate(getCurrentDate());
                biodataHelper.insert(b);

                // lalu dikirim melalui RESLUT_ADD
                setResult(RESULT_ADD);
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;

            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    // tombol back pada handphone
    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }


    // menampilkan alert dialog jika menekan tombol back
    // pada handphone maupun pada ActionBar
    final int ALERT_DIALOG_CLOSE = 10;
    final int ALERT_DIALOG_DELETE = 20;

    private void showAlertDialog(int type) {

        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Anda Yakin Ingin Membatalkan Perubahan Form Ini ?";

        } else {
            dialogTitle = "Hapus";
            dialogMessage = "Anda Yakin Ingin Menghapus Item Ini ?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (isDialogClose) {
                            finish();

                        } else {
                            biodataHelper.delete(biodata.getId());
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    // get time dengan format sebagai berikut
    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }
}
