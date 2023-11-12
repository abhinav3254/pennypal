package com.example.pennypal;



// HomeFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pennypal.recyclerview.MyAdapter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize data
        dataList = new ArrayList<>();
        dataList.add("Java");
        dataList.add("Python");
        dataList.add("JavaScript");
        dataList.add("C++");
        dataList.add("C#");
        dataList.add("Ruby");
        dataList.add("Swift");
        dataList.add("Kotlin");
        dataList.add("TypeScript");
        dataList.add("Go");
        dataList.add("Rust");
        dataList.add("Dart");
        dataList.add("PHP");
        dataList.add("HTML");
        dataList.add("CSS");
        dataList.add("SQL");
        dataList.add("Objective-C");
        dataList.add("Scala");
        dataList.add("R");
        dataList.add("Shell Script");

        // Initialize adapter and set it to RecyclerView
        adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
