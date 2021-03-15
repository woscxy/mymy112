package edu.dali;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.appcompat.app.AppCompatActivity;

public class xiezhu extends AppCompatActivity {
    BottomSheetBehavior bt_behaovior;
    Button bt_more_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiezhu);
//        View bottomSheet=findViewById(R.id.bottom_sheet);
//        bt_behaovior=BottomSheetBehavior.from(bottomSheet);
//        bt_more_result=(Button)findViewById(R.id.button_get_more_results);
//        bt_more_result.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              bt_behaovior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
    }
}