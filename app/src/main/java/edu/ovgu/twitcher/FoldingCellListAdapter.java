package edu.ovgu.twitcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ramotion.foldingcell.FoldingCell;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

public class FoldingCellListAdapter extends ArrayAdapter<Bird> {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public FoldingCellListAdapter(Context context, List<Bird> objects) {
        super(context, 0, objects);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Bird item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.image = cell.findViewById(R.id.img);
            viewHolder.thumbNailImage = cell.findViewById(R.id.imgThumbNail);
            viewHolder.time = cell.findViewById(R.id.time_value);
            viewHolder.date = cell.findViewById(R.id.date_value);
            viewHolder.contentName = cell.findViewById(R.id.name);
            viewHolder.titleName = cell.findViewById(R.id.title_name);
            viewHolder.wiki = cell.findViewById(R.id.wiki_value);
            viewHolder.category = cell.findViewById(R.id.category_value);
            viewHolder.notes = cell.findViewById(R.id.notes_value);
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder
        viewHolder.contentName.setText(item.getBirdName());
        viewHolder.titleName.setText(item.getBirdName());
        viewHolder.time.setText("time");
        SimpleDateFormat formatter4=new SimpleDateFormat("MMM dd yyyy");

        viewHolder.date.setText(formatter4.format(item.getDate()));
        viewHolder.wiki.setText(item.getWikiLink());
        viewHolder.image.setImageBitmap(item.getBitmap());
        viewHolder.thumbNailImage.setImageBitmap(item.getBitmap());
        viewHolder.category.setText(String.valueOf(item.getCategory()));
        viewHolder.notes.setText(item.getNotes());

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }


    // View lookup cache
    private static class ViewHolder {

        ImageView image,thumbNailImage;
        TextView contentName;
        TextView titleName;
        TextView date;
        TextView time;
        TextView wiki;
        TextView category;
        TextView notes;
    }
}
