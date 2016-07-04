package softikoda.com.quotes;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import Quote.Child;
import Quote.Parent;

/**
 * Created by Geofrey on 5/17/2016.
 */
public class CustomExpandableList extends BaseExpandableListAdapter{
    private Context context;
    private ArrayList<Parent> parentList;
    private ArrayList<Parent> originalList;

    public CustomExpandableList(Context context, ArrayList<Parent> parentUserList) {
        this.context = context;
        this.parentList = new ArrayList<Parent>();
        this.parentList.addAll(parentList);
        this.originalList = new ArrayList<Parent>();
        this.originalList.addAll(parentUserList);
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> childList = parentList.get(groupPosition).getChildData();
        return childList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Child> childList = parentList.get(groupPosition).getChildData();
        return childList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Parent parentData = (Parent) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent, null);
        }

        TextView genre_author = (TextView) convertView.findViewById(R.id.genre_author);
        TextView no_of_quotes = (TextView) convertView.findViewById(R.id.no_quotes);


        genre_author.setText(parentData.getGenre_author().trim());
        no_of_quotes.setText(""+parentData.getNo_of_Quotes()+" Quotes ");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Child child = (Child) getChild(groupPosition,childPosition);
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child, null);
        }

        TextView quote = (TextView) convertView.findViewById(R.id.quote);
        TextView author = (TextView) convertView.findViewById(R.id.author);

        quote.setText(child.getQuote().trim());
        author.setText(child.getAuthor().trim());

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return super.getCombinedChildId(groupId, childId);
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return super.getCombinedGroupId(groupId);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return super.getChildType(groupPosition, childPosition);
    }

    @Override
    public int getChildTypeCount() {
        return super.getChildTypeCount();
    }

    public void filterData(String query){

        query = query.toLowerCase();
        Log.v("MyListAdapter", String.valueOf(parentList.size()));
        parentList.clear();

        if(query.isEmpty()){
            parentList.addAll(originalList);
        }
        else {

            for(Parent parent: originalList){

                ArrayList<Child> childList = parent.getChildData();
                ArrayList<Child> newList = new ArrayList<Child>();
                for(Child child: childList){
                    if(child.getAuthor().toLowerCase().contains(query) ||
                            child.getQuote().toLowerCase().contains(query)){
                        newList.add(child);
                    }
                }
                if(newList.size() > 0){
                    Parent p = new Parent(parent.getGenre_author(),parent.getNo_of_Quotes(),newList);
                    parentList.add(p);
                }
            }
        }

        Log.v("MyListAdapter", String.valueOf(parentList.size()));
        notifyDataSetChanged();

    }
}
