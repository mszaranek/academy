package solutions.autorun.academy.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.RevisionDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class SyncServiceImpl implements SyncService {

    private final Logger log = LoggerFactory.getLogger(SyncServiceImpl.class);
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    @Scheduled(fixedDelay = 3600000L)
    public void sync() throws IOException{

        AuditReader reader = AuditReaderFactory.get(entityManager);
        String revisionDate;

        try {
            Number revision = reader.getRevisionNumberForDate(new Date(Long.MAX_VALUE));
            Date date = reader.getRevisionDate(revision);
            revisionDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
            log.info(revisionDate);
        }
        catch(RevisionDoesNotExistException e){
            revisionDate = "1900/01/01 00:00";
        }

        String command = "java -jar jirataskmanager-0.0.1-SNAPSHOT.jar BMA-event "+ revisionDate;
        log.info("Running " + command);
        Runtime run = Runtime.getRuntime();
        Process proc = run.exec(command);

        command = "java -jar trello-wrapper.jar bma " + revisionDate;
        log.info("Running " + command);
        Runtime run1 = Runtime.getRuntime();
        Process proc1 = run1.exec(command);

    }
}
