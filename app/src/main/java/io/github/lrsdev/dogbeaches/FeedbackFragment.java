package io.github.lrsdev.dogbeaches;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by rickiekewene on 20/09/15.
 */
public class FeedbackFragment extends Fragment
{
    Button sendEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        sendEmail = (Button) v.findViewById(R.id.feedback_email_button);
        sendEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:lrsdev.op@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Mobile App Feedback");
                startActivity(Intent.createChooser(intent, "Send Email Feedback"));
            }
        });
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Feedback");
    }
}
