package services;

import dao.AggregationsDao;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.errors.OrderError;
import org.bson.types.ObjectId;

public class AggregationsServicesImpl implements AggregationsServices{

    private final AggregationsDao dao;

    @Inject
    public AggregationsServicesImpl(AggregationsDao dao) {
        this.dao = dao;
    }


    @Override
    public Either<OrderError, String> ExA() {
        return dao.ExA();
    }

    @Override
    public Either<OrderError, String> ExB(ObjectId id) {
        return dao.ExB(id);
    }

    @Override
    public Either<OrderError, String> ExC() {
        return dao.ExC();
    }

    @Override
    public Either<OrderError, String> ExD() {
        return dao.ExD();
    }

    @Override
    public Either<OrderError, String> ExE() {
        return dao.ExE();
    }

    @Override
    public Either<OrderError, String> ExF() {
        return dao.ExF();
    }

    @Override
    public Either<OrderError, String> ExG(ObjectId id) {
        return dao.ExG(id);
    }

    @Override
    public Either<OrderError, String> ExH() {
        return dao.ExH();
    }

    @Override
    public Either<OrderError, String> ExI() {
        return dao.ExI();
    }

    @Override
    public Either<OrderError, String> ExJ() {
        return dao.ExJ();
    }

    @Override
    public Either<OrderError, String> ExK() {
        return dao.ExK();
    }

    @Override
    public Either<OrderError, String> ExL() {
        return dao.ExL();
    }

    @Override
    public Either<OrderError, String> ExM() {
        return dao.ExM();
    }

    @Override
    public Either<OrderError, String> Ex1part2(String species) {
        return dao.Ex1part2(species);
    }

    @Override
    public Either<OrderError, String> Ex2part2() {
        return dao.Ex2part2();
    }

    @Override
    public Either<OrderError, String> Ex3part2() {
        return dao.Ex3part2();
    }

    @Override
    public Either<OrderError, String> Ex4part2(int id) {
        return dao.Ex4part2(id);
    }

    @Override
    public Either<OrderError, String> Ex5part2(int id, String species) {
        return dao.Ex5part2(id, species);
    }
}
