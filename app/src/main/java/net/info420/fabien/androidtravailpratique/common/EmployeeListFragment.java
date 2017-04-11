package net.info420.fabien.androidtravailpratique.common;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.EmployeeAdapter;

public class EmployeeListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = EmployeeListFragment.class.getName();

  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;

  // private Cursor cursor;
  private EmployeeAdapter employeeAdapter;

  // private ListView lvEmployeeList;
  private TextView tvNoEmployee;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_employee_list, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData();

    registerForContextMenu(getListView());
  }

  // Ouvre les détails d'une tâche lorsqu'appuyé
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Log.d(TAG, "onListItemClick : " + id);

    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(getContext(), EmployeeActivity.class);
    Uri employeeUri = Uri.parse(TaskerContentProvider.CONTENT_URI_EMPLOYEE + "/" + id);
    i.putExtra(TaskerContentProvider.CONTENT_ITEM_TYPE_EMPLOYEE, employeeUri);

    startActivity(i);
  }

  private void fillData() {
    // Affiche les champs de la base de données (name)
    String[] from = new String[]{Employee.KEY_name, Employee.KEY_job};

    // Où on affiche les champs
    int[] to = new int[]{R.id.tv_employee_name, R.id.tv_task_description};

    getLoaderManager().initLoader(0, null, this);
    employeeAdapter = new EmployeeAdapter(getContext(), R.layout.employee_row, null, from, to, 0, (TaskerApplication) getActivity().getApplication());

    setListAdapter(employeeAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = {Employee.KEY_ID, Employee.KEY_name, Employee.KEY_job};
    CursorLoader cursorLoader = new CursorLoader(getContext(), TaskerContentProvider.CONTENT_URI_EMPLOYEE, projection, null, null, null);

    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    employeeAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    // Les données ne sont plus valides
    employeeAdapter.swapCursor(null);
  }
}