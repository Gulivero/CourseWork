package slavin.fit.bstu.humanresourcesdepartment.Remote;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import slavin.fit.bstu.humanresourcesdepartment.Model.Worker;

public interface IMyAPI {
    //http://localhost:5000/api/login
    @POST("api/register")
    Observable<String> registerUser(@Body Worker user);

    @POST("api/login")
    Observable<String> loginUser(@Body Worker user);

    @POST("api/update")
    Observable<String> updateUser(@Body Worker user);

    @POST("api/delete")
    Observable<String> deleteUser(@Body Worker user);
}
