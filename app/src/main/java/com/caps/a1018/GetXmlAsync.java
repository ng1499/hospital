package com.caps.a1018;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

interface OnCompleteListener {
    void onComplete(List<Hospital> list);
}

public class GetXmlAsync extends AsyncTask<Void, Void, List<Hospital>> {
    private final OnCompleteListener onCompleteListener;
    private LatLng latLng;

    GetXmlAsync(OnCompleteListener onCompleteListener, LatLng latLng) {
        this.onCompleteListener = onCompleteListener;
        this.latLng = latLng;
    }

    @Override
    protected List<Hospital> doInBackground(Void... voids) {
        List<Hospital> hospitalList = new ArrayList<>();
        System.out.println((this.latLng).latitude);
        System.out.println((this.latLng).longitude);

        String queryUrl =
                "http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire?serviceKey=E97ZXy9iUSGFPgKniLgT73iIKXuE61D56%2Fhz2YlUJzet8DU61oDlIOlnOf3GD6lU%2BiHEUv96oEGOUZwc60XW%2Fw%3D%3D" +
                        "&WGS84_LON="+ (this.latLng).longitude +
                        "&WGS84_LAT=" + (this.latLng).latitude +
                        "&pageNo=1&numOfRows=20";
        //requestGet(queryUrl);
        try {
            URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성
            InputStream is = url.openStream(); // url위치로 인풋스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            // inputstream 으로부터 xml 입력받기
            xpp.setInput(new InputStreamReader(is, "UTF-8"));
            String tag;
            int eventType = xpp.getEventType();

            String latitude = "", longitude = "", distance = "", name = "", address = "", phone = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        // 파싱 시작
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); // 태그 이름 얻어오기
                        if (tag.equals("item")) ; // 첫번째 검색결과
                        else if (tag.equals("distance")) {
                            xpp.next();
                            distance = xpp.getText();
                        } else if (tag.equals("dutyAddr")) {
                            xpp.next();
                            address = xpp.getText();
                        } else if (tag.equals("dutyName")) {
                            xpp.next();
                            name = xpp.getText();
                        } else if (tag.equals("dutyTel1")) {
                            xpp.next();
                            phone = xpp.getText();
                        } else if (tag.equals("latitude")) {
                            xpp.next();
                            latitude = xpp.getText();
                        } else if (tag.equals("longitude")) {
                            xpp.next();
                            longitude = xpp.getText();
                        }

                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); // 태그 이름 얻어오기
                        if (tag.equals("item")) {
                            hospitalList.add(new Hospital(
                                    name, address, phone, latitude, longitude, distance
                            ));

                            latitude = "";
                            longitude = "";
                            distance = "";
                            name = "";
                            address = "";
                            phone = "";
                        }
                        break;
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hospitalList;
    }


    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    protected void onPostExecute(List<Hospital> result) {
        onCompleteListener.onComplete(result);
    }
}