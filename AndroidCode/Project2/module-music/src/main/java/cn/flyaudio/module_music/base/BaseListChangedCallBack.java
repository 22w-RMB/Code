package cn.flyaudio.module_music.base;

import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

public class BaseListChangedCallBack<A extends RecyclerView.Adapter, L> extends ObservableList.OnListChangedCallback<ObservableList<L>>{

    WeakReference<A> weakReference;

    public BaseListChangedCallBack(A adapter) {
        weakReference = new WeakReference<A>(adapter);
    }

    @Override
    public void onChanged(ObservableList<L> sender) {

    }

    @Override
    public void onItemRangeChanged(ObservableList<L> sender, int positionStart, int itemCount) {
        if (weakReference.get() != null)
            weakReference.get().notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList<L> sender, int positionStart, int itemCount) {
        if (weakReference.get() != null)
            weakReference.get().notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList<L> sender, int fromPosition, int toPosition, int itemCount) {
        if (weakReference.get() != null)
            weakReference.get().notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRangeRemoved(ObservableList<L> sender, int positionStart, int itemCount) {
        if (weakReference.get() != null)
            weakReference.get().notifyItemRangeRemoved(positionStart, itemCount);
    }
}
