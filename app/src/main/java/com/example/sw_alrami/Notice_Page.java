package com.example.sw_alrami;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Notice_Page extends Fragment {

    private ListView mListView;
    private Notice_Adapter mNoticeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.notice_page, container, false);
        mListView = view.findViewById(R.id.listView); // 리스트뷰 초기화
        mNoticeAdapter = new Notice_Adapter(); // 어댑터 초기화
        dataSetting(); // 데이터 설정 메서드 호출
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void dataSetting() {
        for (int i = 0; i < 10; i++) {
            mNoticeAdapter.addItem( "name_" + i, "contents_" + i);
        }
        mListView.setAdapter(mNoticeAdapter); // 어댑터를 리스트뷰에 설정
    }
}