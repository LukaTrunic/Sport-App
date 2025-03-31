package rs.ac.singidunum.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    // Lists to hold the fragments and their titles
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    // Constructor that initializes the adapter with the fragment activity
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Method to add a fragment and its title to the lists
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    // Method to create and return the fragment at the specified position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    // Method to return the total number of fragments
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    // Method to return the title of the fragment at the specified position
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    // Method to return the fragment at the specified position
    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }
}
