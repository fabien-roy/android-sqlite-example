package net.info420.fabien.androidtravailpratique.common;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import net.info420.fabien.androidtravailpratique.R;
import net.info420.fabien.androidtravailpratique.contentprovider.TaskerContentProvider;
import net.info420.fabien.androidtravailpratique.utils.Employee;
import net.info420.fabien.androidtravailpratique.utils.EmployeeAdapter;

public class EmployeeListActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
  private final static String TAG = EmployeeListActivity.class.getName();

  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;

  // private Cursor cursor;
  private EmployeeAdapter employeeAdapter;

  // private ListView lvEmployeeList;
  private TextView tvNoEmployee;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_list);

    this.getListView().setDividerHeight(2); // TODO : Tester ce que fais ceci
    fillData();

    registerForContextMenu(getListView());

    initUI();
  }

  private void initUI() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_employee_list);

    ((TaskerApplication) getApplication()).setStatusBarColor(this);

    // lvEmployeeList = (ListView) findViewById(R.id.lv_employee_list);
  }

  // Ouvre les détails d'une tâche lorsqu'appuyé
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Log.d(TAG, "onListItemClick : " + id);

    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(this, EmployeeActivity.class);
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
    employeeAdapter = new EmployeeAdapter(this, R.layout.employee_row, null, from, to, 0, (TaskerApplication) getApplication());

    setListAdapter(employeeAdapter);
  }

  // Création d'un nouveau Loader
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = {Employee.KEY_ID, Employee.KEY_name, Employee.KEY_job};
    CursorLoader cursorLoader = new CursorLoader(this, TaskerContentProvider.CONTENT_URI_EMPLOYEE, projection, null, null, null);

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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_employee_list, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_task_list:
        // TODO : Vérifier si c'est la dernière activité?
        // Je termine l'activité, car en se fiant à l'aborescence de l'application, on a pas le choix de venir de MainActivity
        finish();
        break;
      case R.id.menu_add:
        startActivity(new Intent(this, NewEmployeeActivity.class));
        break;
      case R.id.menu_prefs:
        startActivity(new Intent(this, PrefsActivity.class));
        break;
      default:
        break;
    }
    return true;
  }
}