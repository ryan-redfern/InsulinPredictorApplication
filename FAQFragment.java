package uk.ac.stir.cs.insulinpredictorapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FAQFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }
    @Override
    /**
     * When questions are clicked,
     * answers are set to invisible or visible
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView tvQuestionOne = view.findViewById(R.id.tvQuestionOne);
        TextView tvAnswerOne = view.findViewById(R.id.tvAnswerOne);
        TextView tvQuestionTwo = view.findViewById(R.id.tvQuestionTwo);
        TextView tvAnswerTwo = view.findViewById(R.id.tvAnswerTwo);
        TextView tvQuestionThree = view.findViewById(R.id.tvQuestionThree);
        TextView tvAnswerThree = view.findViewById(R.id.tvAnswerThree);
        TextView tvQuestionFour = view.findViewById(R.id.tvQuestionFour);
        TextView tvAnswerFour = view.findViewById(R.id.tvAnswerFour);
        tvQuestionOne.setOnClickListener(view1 -> {

            if (tvAnswerOne.getVisibility() == View.VISIBLE){
                tvAnswerOne.setVisibility(View.INVISIBLE); // sets answer to invisible
            } else {
                tvAnswerOne.setVisibility(View.VISIBLE);
            }



        });
        tvQuestionTwo.setOnClickListener(view1 -> {

            if (tvAnswerTwo.getVisibility() == View.VISIBLE){
                tvAnswerTwo.setVisibility(View.INVISIBLE);
            } else {
                tvAnswerTwo.setVisibility(View.VISIBLE);
            }



        });
        tvQuestionThree.setOnClickListener(view1 -> {

            if (tvAnswerThree.getVisibility() == View.VISIBLE){
                tvAnswerThree.setVisibility(View.INVISIBLE);
            } else {
                tvAnswerThree.setVisibility(View.VISIBLE);
            }



        });
        tvQuestionFour.setOnClickListener(view1 -> {

            if (tvAnswerFour.getVisibility() == View.VISIBLE){
                tvAnswerFour.setVisibility(View.INVISIBLE);
            } else {
                tvAnswerFour.setVisibility(View.VISIBLE);
            }



        });

    }
}
