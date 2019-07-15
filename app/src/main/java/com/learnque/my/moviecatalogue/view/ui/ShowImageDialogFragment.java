package com.learnque.my.moviecatalogue.view.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.learnque.my.moviecatalogue.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowImageDialogFragment extends DialogFragment {


    public ShowImageDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //avoid a margin/padding on the top of dialog box
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_image_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.showPoster);

        String image = getArguments().getString("dataPoster");

        Glide.with(getActivity().getApplicationContext())
                .load(image)
                .apply(new RequestOptions().override(100, 150))
                .into(imageView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toast.makeText(context, "Click Back Button for Back", Toast.LENGTH_SHORT).show();
    }
}
