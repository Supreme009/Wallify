package wallify.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private int[] wallpapers;

    public GridAdapter(Context context, int[] wallpapers) {
        this.context = context;
        this.wallpapers = wallpapers;
    }

    @Override
    public int getCount() {
        return wallpapers.length;
    }

    @Override
    public Object getItem(int position) {
        return wallpapers[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true); // 🔹 Ajusta automáticamente alto/alto
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);

            // 🔹 Usa match_parent en ancho para que el GridView lo divida bien en 2 columnas
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    500 // altura fija razonable, puedes subirla o bajarla
            ));
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(wallpapers[position]);
        return imageView;
    }
}