package com.cgm.checklist.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemListener implements RecyclerView.OnItemTouchListener {

    private RecyclerTouchListener listener;
    private GestureDetector gd;

    public interface RecyclerTouchListener {
        public void onClickListener(View view, int position);
    }

    public RecyclerItemListener(Context context, final RecyclerView rv, final RecyclerTouchListener listener) {
        this.listener = listener;
        gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                View view = rv.findChildViewUnder(event.getX(), event.getY());
                listener.onClickListener(view, rv.getChildAdapterPosition(view));
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View chield = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        return (chield != null && gd.onTouchEvent(motionEvent));
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
