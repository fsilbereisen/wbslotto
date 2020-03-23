/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.wbstraining.lotto.cache;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import de.wbstraining.lotto.persistence.dao.KundeFacadeLocal;
import de.wbstraining.lotto.persistence.dao.ZiehungFacadeLocal;
import de.wbstraining.lotto.persistence.model.Kunde;
import de.wbstraining.lotto.persistence.model.Ziehung;

/**
 *
 * @author gz1
 */
@Singleton
@Startup
public class DBCache implements DBCacheLocal {
    
    private List<Kunde> kunden;
    private Map<LocalDate, Ziehung> ziehungenByDatum;
    
    @EJB
    private KundeFacadeLocal kundeFacadeLocal;
    
    @EJB
    private ZiehungFacadeLocal ziehungFacadeLocal;

    @Override
    public Kunde randomKunde() {
        return kunden.get(ThreadLocalRandom.current().nextInt(kunden.size()));
    }

    @Override
    public Ziehung ziehungByDatum(Date datum) {
        return ziehungenByDatum.get(date2LocalDate(datum));
    }

    @PostConstruct
    @Override
    public void loadKundenUndZiehungen() {
        kunden = kundeFacadeLocal.findAll();
        ziehungenByDatum = new HashMap<>();
        for(Ziehung ziehung : ziehungFacadeLocal.findAll()) {
            ziehungenByDatum.put(date2LocalDate(ziehung.getZiehungsdatum()), ziehung);
        }
    }
    
    private LocalDate date2LocalDate(Date datum) {
    	return Instant.ofEpochMilli(datum.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
