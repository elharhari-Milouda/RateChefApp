package ma.tp04.ratechiefs.service;

import java.util.ArrayList;
import java.util.List;

import ma.tp04.ratechiefs.beans.Chief;
import ma.tp04.ratechiefs.beans.Plat;
import ma.tp04.ratechiefs.dao.IDao;

public class PlatService implements IDao<Plat> {
    private List<Plat> plats;
    private static PlatService instance;
    public PlatService() {
        this.plats = new ArrayList<>();
    }
    public static PlatService getInstance() {
        if(instance == null)
            instance = new PlatService();
        return instance;
    }

    @Override
    public boolean create(Plat o) {
        return plats.add(o);
    }

    @Override
    public boolean update(Plat o) {
        return false;
    }

    @Override
    public boolean delete(Plat o) {
        return plats.remove(o);
    }

    @Override
    public Plat findById(int id) {
        for(Plat s : plats){
            if(s.getId() == id)
                return s;
        }
        return null;
    }

    @Override
    public List<Plat> findAll() {
        return plats;
    }

}
