package ma.tp04.ratechiefs.service;

import java.util.ArrayList;
import java.util.List;

import ma.tp04.ratechiefs.beans.Chief;
import ma.tp04.ratechiefs.dao.IDao;

public class ChiefService implements IDao<Chief>{
    private List<Chief> chiefs;
    private static ChiefService instance;
    private ChiefService() {
        this.chiefs = new ArrayList<>();
    }
    public static ChiefService getInstance() {
        if(instance == null)
            instance = new ChiefService();
        return instance;
    }
    @Override
    public boolean create(Chief o) {
        return chiefs.add(o);
    }
    @Override
    public boolean update(Chief o) {
        for(Chief s : chiefs){
            if(s.getId() == o.getId()){
                s.setImg(o.getImg());
                s.setName(o.getName());
                s.setStar(o.getStar());
            }
        }
        return true;
    }
    @Override
    public boolean delete(Chief o) {
        return chiefs.remove(o);
    }
    @Override
    public Chief findById(int id) {
        for(Chief s : chiefs){
            if(s.getId() == id)
                return s;
        }
        return null;
    }
    @Override
    public List<Chief> findAll() {
        return chiefs;
    }
}
