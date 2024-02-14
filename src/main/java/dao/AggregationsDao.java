package dao;

import io.vavr.control.Either;
import model.errors.OrderError;
import org.bson.types.ObjectId;

public interface AggregationsDao {
    Either<OrderError, String> ExA();
    Either<OrderError, String> ExB(ObjectId id);
    Either<OrderError, String> ExC();
    Either<OrderError, String> ExD();
    Either<OrderError, String> ExE();
    Either<OrderError, String> ExF();
    Either<OrderError, String> ExG(ObjectId id);
    Either<OrderError, String> ExH();
    Either<OrderError, String> ExI();
    Either<OrderError, String> ExJ();
    Either<OrderError, String> ExK();
    Either<OrderError, String> ExL();
    Either<OrderError, String> ExM();

    Either<OrderError, String> Ex1part2(String species);

    Either<OrderError, String> Ex2part2();

    Either<OrderError, String> Ex3part2();

    Either<OrderError, String> Ex4part2(int id);

    Either<OrderError, String> Ex5part2(int id, String species);

}
