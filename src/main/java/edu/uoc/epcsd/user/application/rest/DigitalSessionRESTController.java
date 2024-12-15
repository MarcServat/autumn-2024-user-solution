package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.application.rest.request.CreateDigitalSessionRequest;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.service.DigitalSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/digital")
public class DigitalSessionRESTController {

    private final DigitalSessionService digitalSessionService;

    @GetMapping("/allDigital")
    @ResponseStatus(HttpStatus.OK)
    public List<DigitalSession> getAllDigitalSession() {
        log.trace("getAllDigitalSession");

        return digitalSessionService.findAllDigitalSession();
    }

    
    @GetMapping("/digitalByUser")
    @ResponseStatus(HttpStatus.OK)
    public List<DigitalSession> getDigitalSessionByUser(@RequestParam @NotNull Long userId) {
        log.trace("getDigitalSessionsByUser");
        return digitalSessionService.findDigitalSessionByUser(userId);
    }  
    
    @PostMapping("/addDigital")
    public ResponseEntity<Long> addDigitalSession(@RequestBody @Valid CreateDigitalSessionRequest createDigitalSessionRequest) {
        log.trace("addDigitalSession");

        try {
            log.trace("Creating DigitalSession " + createDigitalSessionRequest);
            Long digitalSessionId = digitalSessionService.addDigitalSession(DigitalSession.builder()
                    .userId(createDigitalSessionRequest.getUserId())
                    .description(createDigitalSessionRequest.getDescription())
                    .location(createDigitalSessionRequest.getLocation())
                    .link(createDigitalSessionRequest.getLink())
                    .build());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{userId}")
                    .buildAndExpand(digitalSessionId)
                    .toUri();

            return ResponseEntity.created(uri).body(digitalSessionId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified userId " + createDigitalSessionRequest.getUserId() + " does not exist.", e);
        }
    }

    @PostMapping("/updateDigital/{digitalSessionId}")
    public ResponseEntity<Boolean> updateDigitalSession(@PathVariable @NotNull Long digitalSessionId, @RequestBody @Valid CreateDigitalSessionRequest updateDigitalSessionRequest) {
        log.trace("updateDigitalSession");
        log.info(digitalSessionId);
        try {
			log.trace("Updating DigitalSession " + updateDigitalSessionRequest);
            digitalSessionService.updateDigitalSession(digitalSessionId, updateDigitalSessionRequest.getDescription(), updateDigitalSessionRequest.getLink(), updateDigitalSessionRequest.getLocation(), updateDigitalSessionRequest.getUserId());

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified Id or UserId " + digitalSessionId +" / "+ updateDigitalSessionRequest.getUserId()+" does not exist.", e);
        }
    }

    @PostMapping("/dropDigital/{digitalSessionId}")
    public ResponseEntity<Boolean> dropDigitalSession(@PathVariable @NotNull Long digitalSessionId) {
        log.trace("dropDigitalSession");
        log.info(digitalSessionId);
        try {
            digitalSessionService.dropDigitalSession(digitalSessionId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified DigitalSession id " + digitalSessionId + " does not exist.", e);
        }
    }    
}
