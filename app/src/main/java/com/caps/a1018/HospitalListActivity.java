package com.caps.a1018;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.caps.a1018.databinding.ActivityListBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.naver.maps.geometry.LatLng;

import java.util.List;

public class HospitalListActivity extends AppCompatActivity implements OnHospitalPhoneClickListener, OnCompleteListener {
    private ActivityListBinding binding;
    private HospitalRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("현재 영업중인 내위치 주변 병원");
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRecyclerView();

        if (checkIfAlreadyHavePermission())
            getMyLocation();
        else
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1111);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_map:
                navigateToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getList(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        GetXmlAsync async = new GetXmlAsync(this, latLng);
        async.execute();
    }

    private void setRecyclerView() {
        adapter = new HospitalRecyclerViewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // 리니어레이아웃 매니저 클래스 사용
        binding.recyclerView.setHasFixedSize(true); // 사이즈 고정
        binding.recyclerView.setLayoutManager(layoutManager); //레이아웃 매니저와 리사이클뷰 연결
        binding.recyclerView.setAdapter(adapter); //어댑터 연결
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,   //구분선추가
                layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onPhotoNumberClickListener(String phoneNumber) { //전화번호 클릭시 넘겨주는함수
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1111) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getMyLocation() { //현재위치 조회 권한 얻기.
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient
                .getLastLocation()
                .addOnSuccessListener(this::getList);

    }

    @Override
    public void onComplete(List<Hospital> list) {
        adapter.submitList(list);
    }
}
