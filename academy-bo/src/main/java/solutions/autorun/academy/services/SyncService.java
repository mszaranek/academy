package solutions.autorun.academy.services;

import org.springframework.stereotype.Service;

import java.io.IOException;


public interface SyncService {

    void sync() throws IOException;

}
