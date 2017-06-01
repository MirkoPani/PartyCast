package com.example.mirko.custombuttonexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mirko.custombuttonexample.customviews.TypeWriter;
import com.example.mirko.custombuttonexample.models.Avatar;


public class AvatarFragment extends Fragment {

    Avatar avatar;
    TypeWriter txtName;
    TypeWriter txtDescription;
    ImageView imgView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_avatar, container, false);
        txtName=(TypeWriter) view.findViewById(R.id.txtName);
        txtDescription=(TypeWriter) view.findViewById(R.id.txtDescription);
        imgView=(ImageView) view.findViewById(R.id.imgAvatar);
        txtName.setText(avatar.getName());
        txtDescription.setText(avatar.getDescription());
        imgView.setImageResource(avatar.getImgSrcId());
        return view;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

}
