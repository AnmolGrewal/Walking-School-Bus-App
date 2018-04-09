package com.cmpt276.project.walkinggroupapp.appactivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmpt276.project.walkinggroupapp.R;
import com.cmpt276.project.walkinggroupapp.model.ModelManager;
import com.cmpt276.project.walkinggroupapp.proxy.ProxyBuilder;

public class ViewPermissionActivity extends AppCompatActivity {
    public static final String PERMISSION_ID = "UserID";

    private long permissionId;

    private ModelManager modelManager;

//    private Permission permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_permission);

        modelManager = ModelManager.getInstance();

        getExtra();

//        ProxyBuilder.SimpleCallback<Permission> getPermissionByIdCallback = returnPermission -> setupViews(returnPermission);
//        modelManager.getPermissionById(ViewPermissionActivity.this, getPermissionByIdCallback, permissionId);
    }

    private void getExtra() {
        Intent intent = getIntent();
        permissionId = intent.getLongExtra(PERMISSION_ID, -1);
    }

//    private void setupViews(Permission returnPermission) {
//        // TODO: setup activity using return permission.
//    }

    public static Intent makeIntent(Context context, Long userId){
        Intent intent = new Intent(context, ViewPermissionActivity.class);
        intent.putExtra(PERMISSION_ID, userId);
        return intent;
    }
}
