package androidx.recyclerview.widget;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * A bind-aware {@link RecyclerView.ViewHolder} implementation that knows when it's bound or
 * unbound.
 * <p>
 * Disclaimer: This is in no way supported and THIS COULD BREAK AT ANY TIME. Left for research.
 *
 * @author hxw
 * @date 2019/1/11
 */
public abstract class BindAwareViewHolder extends RecyclerView.ViewHolder {
    public BindAwareViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected void onBind() {

    }

    protected void onUnbind() {

    }

    @Override
    void setFlags(int flags, int mask) {
        boolean wasBound = isBound();
        super.setFlags(flags, mask);
        notifyBinding(wasBound, isBound());
    }

    @Override
    void addFlags(int flags) {
        boolean wasBound = isBound();
        super.addFlags(flags);
        notifyBinding(wasBound, isBound());
    }

    @Override
    void clearPayload() {
        boolean wasBound = isBound();
        super.clearPayload();
        notifyBinding(wasBound, isBound());
    }

    @Override
    void resetInternal() {
        boolean wasBound = isBound();
        super.resetInternal();
        notifyBinding(wasBound, isBound());
    }

    private void notifyBinding(boolean previousBound, boolean currentBound) {
        if (previousBound && !currentBound) {
            onUnbind();
        } else if (!previousBound && currentBound) {
            onBind();
        }
    }
}
