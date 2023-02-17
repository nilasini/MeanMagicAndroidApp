package com.sample.mealmagic;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.browser.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sample.mealmagic.databinding.FragmentFirstBinding;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import java.util.concurrent.atomic.AtomicReference;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private final AtomicReference<CustomTabsIntent> customTabIntent = new AtomicReference<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signIn.setOnClickListener(new AuthorizeListener());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class AuthorizeListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {

            AuthorizationServiceConfiguration serviceConfiguration =
                    new AuthorizationServiceConfiguration(
                            /* authorize endpoint and token endpoint*/
                            Uri.parse("https://dev.api.asgardeo.io/t/nila1/oauth2/authorize") ,
                            Uri.parse("https://dev.api.asgardeo.io/t/nila1/oauth2/token")
                    );
            String clientId = "WsbEf891AyTyI68q0XX5mzRd2Eka";
            Uri redirectUri = Uri.parse("com.example.myapplication://oauth");
            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                    serviceConfiguration,
                    clientId,
                    ResponseTypeValues.CODE,
                    redirectUri
            );
            builder.setScopes("openid","profile");
            AuthorizationRequest request = builder.build();
            AuthorizationService authorizationService = new AuthorizationService(view.getContext());
            CustomTabsIntent.Builder intentBuilder = authorizationService.
                    createCustomTabsIntentBuilder(request.toUri());

            customTabIntent.set(intentBuilder.build());

            Intent completionIntent = new Intent(view.getContext(), UserInfoActivity.class);
            Intent cancelIntent = new Intent(view.getContext(), MainActivity.class);

            authorizationService.performAuthorizationRequest(request, PendingIntent.
                            getActivity(view.getContext(), 0,
                                    completionIntent, 0), PendingIntent.getActivity(view.getContext(),
                            0, cancelIntent, 0),
                    customTabIntent.get());
        }
    }

}