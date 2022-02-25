package com.fcalo.translator;

import com.fcalo.translator.Dto.TranslationInDto;
import com.fcalo.translator.models.Translation;
import com.fcalo.translator.models.TranslationCategory;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;

import java.util.List;

import static java.lang.Long.parseLong;

@ApplicationScoped
public class TranslatorCtrl {
    @Inject
    EntityManager em;

    @Inject
    Session session;

    private static final Logger logger = Logger.getLogger(TranslatorCtrl.class.getName());

    @Route(path = "category", methods = Route.HttpMethod.GET, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    public List<TranslationCategory> getAllCategories() {
        logger.info("calling getAllCategories");
        return em.createNamedQuery("TranslationCategory.findAll", TranslationCategory.class)
                .getResultList();
    }

    @Route(path = "category/:id", methods = Route.HttpMethod.GET, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    public void getSingleCategories(RoutingExchange ex) {
        getSingle(ex, TranslationCategory.class);
    }


    @Route(path = "category", methods = Route.HttpMethod.POST, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    @Transactional
    public void addCategory(RoutingContext routingContext) {
        logger.info("calling addCategory");
        var payload = routingContext.getBodyAsJson().mapTo(TranslationCategory.class);
        em.persist(payload);
        em.flush();

        routingContext.json(payload);
    }

    @Route(path = "category/:id", methods = Route.HttpMethod.PUT, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    @Transactional
    public void updateCategory(RoutingContext routingContext) {
        var payload = routingContext.getBodyAsJson().mapTo(TranslationCategory.class);
        var id = routingContext.pathParam("id");
        Long numberId = null;

        try {
            numberId = parseLong(id);
            logger.info("calling updateCategory with id -> " + numberId);
        } catch (NumberFormatException e) {
            routingContext.response().setStatusCode(400).end("wrong pathParam format: the parameter needs to be a number");
        }

        TranslationCategory entity = em.find(TranslationCategory.class, numberId);
        if (entity == null) {
            routingContext.response().setStatusCode(404).end("Not found");
        }
        entity.setName(payload.getName());

        em.persist(entity);
        em.flush();

        routingContext.json(entity);
    }

    @Route(path = "category/:id", methods = Route.HttpMethod.DELETE, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    @Transactional
    public void removeSingleCategories(RoutingContext routingContext) {
        removeSingle(routingContext, TranslationCategory.class);
    }

    @Route(path = "translation/category/:category", methods = Route.HttpMethod.GET, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    public void getAllTranslationsByCategory(RoutingContext routingContext) {
        var category_id = routingContext.pathParam("category");
        Long categoryId = null;
        try {
            categoryId = parseLong(category_id);
            logger.info("calling getAllTranslationsByCategory with category id -> " + categoryId);
        } catch (NumberFormatException e) {
            routingContext.response().setStatusCode(400).end("wrong pathParam format: the parameter needs to be a number");
        }

        var query = em.createNamedQuery("Translation.findAll", Translation.class);
        query.setParameter("category_id", categoryId);
        var results = query.getResultList();

        routingContext.json(results);
    }

    @Route(path = "translation/:id", methods = Route.HttpMethod.GET, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    public void getSingleTranslations(RoutingExchange ex) {
        getSingle(ex, Translation.class);
    }

    @Route(path = "translation", methods = Route.HttpMethod.POST, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    @Transactional
    public void addTranslation(RoutingContext routingContext) {
        logger.info("calling addTranslation");
        var payload = routingContext.getBodyAsJson().mapTo(TranslationInDto.class);
        var cat_id = payload.getCategoryId();
        var category = em.find(TranslationCategory.class, cat_id);
        var ent = Translation.builder()
                .en(payload.getEn())
                .fr(payload.getFr())
                .it(payload.getIt())
                .category(category).build();

        em.persist(ent);
        em.flush();

        routingContext.json(ent);
    }

    @Route(path = "translation/:id", methods = Route.HttpMethod.DELETE, type = Route.HandlerType.BLOCKING, produces = MediaType.APPLICATION_JSON)
    @Transactional
    public void removeSingleTranslation(RoutingContext routingContext) {
        removeSingle(routingContext, Translation.class);
    }

    private <T> void getSingle(RoutingExchange ex, Class<T> tClass) {
        var id = ex.getParam("id").orElse(null);
        Long numberId = null;
        try {
            numberId = parseLong(id);
            logger.info("calling getSingle with id -> " + numberId + " |  and class " + tClass.getName());
        } catch (NumberFormatException e) {
            ex.response().setStatusCode(400).end("wrong pathParam format: the parameter needs to be a number");
        }

        T entity = em.find(tClass, numberId);
        if (entity == null) {
            ex.notFound();
        }
        ex.ok(Json.encode(entity));
    }

    private <T> void removeSingle(RoutingContext routingContext, Class<T> tClass) {
        var id = routingContext.pathParam("id");
        Long numberId = null;

        try {
            numberId = parseLong(id);
            logger.info("calling removeSingle with id -> " + numberId + "  | and class " + tClass.getName());
        } catch (NumberFormatException e) {
            routingContext.response().setStatusCode(400).end("wrong pathParam format: the parameter needs to be a number");
        }

        T entity = em.find(tClass, numberId);
        if (entity == null) {
            routingContext.response().setStatusCode(404).end("Not found");
        }
        try {
            em.remove(entity);
            em.flush();
            routingContext.response().setStatusCode(204).end();
        } catch (Exception e) {
            routingContext.response().setStatusCode(400).end(e.getMessage());
        }
    }
}