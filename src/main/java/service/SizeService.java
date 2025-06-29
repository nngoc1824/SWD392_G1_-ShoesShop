package service;

import dao.SizeDAO;
import entites.Size;

import java.util.List;

public class SizeService {
    private SizeDAO sizeDAO;

    public SizeService() {
        this.sizeDAO = new SizeDAO();
    }

    public List<Size> getAllSizes() {
        return sizeDAO.getAllSizes();
    }

    public int insertSize(String newSize) {
        return sizeDAO.insertAndGetId(newSize);
    }
}
