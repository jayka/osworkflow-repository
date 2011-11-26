/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.ejb3;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.Remove;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.spi.PersistenceUnitTransactionType;

import com.opensymphony.module.propertyset.AbstractPropertySet;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertyImplementationException;
import com.opensymphony.util.Data;
import com.opensymphony.util.XMLUtils;
import org.w3c.dom.Document;

/**
 * EJB3 propertyset implementation.
 * This implementation requires a couple of extra init args:
 * <li><code>manager</code>: Entity manager to use.
 * <li><code>transaction</code>: Can be either JTA or RESOURCE_LOCAL.
 * <p/>
 * Note that this class can also be deployed as a stateful EJB3 session bean. In that case,
 * no configuration is required. It should also not be obtained via PropertySetManager,
 * but should instead be looked up in the container. The name the bean is deployed under
 * is 'OSPropertySet'. Before any operations are called on the stateful bean, {@link #setEntityId(Long)}
 * and {@link #setEntityName(String)} must be called.
 *
 * @author Hani Suleiman
 *         Date: Nov 8, 2005
 *         Time: 4:51:53 PM
 */
@Stateful(name = "OSPropertySet")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EJBPropertySetImpl extends AbstractPropertySet implements EJBPropertySet
{
  private EntityManager entityManager;
  private Long entityId;
  private PersistenceUnitTransactionType transactionType;
  private String entityName;
  private boolean inContainer;

  @PersistenceContext(unitName = "propertyset")
  private EntityManager injectedEntityManager;

  public EJBPropertySetImpl()
  {
  }

  public EJBPropertySetImpl(EntityManager entityManager, PersistenceUnitTransactionType transactionType)
  {
    this.entityManager = entityManager;
    this.transactionType = transactionType;
  }

  public EJBPropertySetImpl(EntityManager entityManager, PersistenceUnitTransactionType transactionType, Long entityId, String entityName)
  {
    this.entityManager = entityManager;
    this.entityId = entityId;
    this.entityName = entityName;
    this.transactionType = transactionType;
  }

  @PostConstruct
  public void checkInjection()
  {
    entityManager = injectedEntityManager;
    inContainer = true;
  }

  @Remove
  public void destroy()
  {
    entityManager = null;
    injectedEntityManager = null;
    entityId = null;
    entityName = null;
  }
  
  //~ Methods ////////////////////////////////////////////////////////////////

  public void setEntityId(Long entityId)
  {
    this.entityId = entityId;
  }

  public Long getEntityId()
  {
    return entityId;
  }

  public void setEntityManager(EntityManager entityManager)
  {
    this.entityManager = entityManager;
  }

  public EntityManager getEntityManager()
  {
    return entityManager;
  }

  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }

  public String getEntityName()
  {
    return entityName;
  }

  public void clear()
  {
    entityManager.clear();
  }

  @TransactionAttribute
  public Collection getKeys() throws PropertyException {
    return super.getKeys();
  }

  @TransactionAttribute
  public Collection getKeys(int type) throws PropertyException {
    return super.getKeys(type);
  }

  @TransactionAttribute
  public Collection getKeys(String prefix) throws PropertyException {
    return super.getKeys(prefix);
  }

  @TransactionAttribute
  public Collection getKeys(String prefix, int type) throws PropertyException
  {
    return getKeys(entityName, entityId, prefix, type);
  }

  @TransactionAttribute
  public Collection getKeys(String entityName, long entityId, String prefix, int type) throws PropertyException
  {
    Query q;

    if((type == 0) && (prefix == null))
    {
      q = entityManager.createNamedQuery("keys");
    }
    //all types with the specified prefix
    else if((type == 0) && (prefix != null))
    {
      q = entityManager.createNamedQuery("keys.prefix");
      q.setParameter("prefix", prefix + '%');
    }
    //type and prefix
    else if((prefix == null) && (type != 0))
    {
      q = entityManager.createNamedQuery("keys.type");
      q.setParameter("type", type);
    }
    else
    {
      q = entityManager.createNamedQuery("keys.prefixAndType");
      q.setParameter("prefix", prefix + '%');
      q.setParameter("type", type);
    }

    q.setParameter("entityId", entityId);
    q.setParameter("entityName", entityName);

    return q.getResultList();
  }

  public void setTransactionType(PersistenceUnitTransactionType transactionType)
  {
    this.transactionType = transactionType;
  }

  public PersistenceUnitTransactionType getTransactionType()
  {
    return transactionType;
  }

  public int getType(String key) throws PropertyException
  {
    return getType(entityName, entityId, key);
  }

  @TransactionAttribute
  public int getType(String entityName, long entityId, String key) throws PropertyException
  {
    EntryPK pk = new EntryPK(entityName, entityId, key);
    PropertyEntry entry = entityManager.find(PropertyEntry.class, pk);

    if(entry == null)
    {
      return 0;
    }

    return entry.getType();
  }

  public boolean exists(String key) throws PropertyException
  {
    return exists(entityName, entityId, key);
  }

  @TransactionAttribute
  public boolean exists(String entityName, long entityId, String key) throws PropertyException
  {
    EntryPK pk = new EntryPK(entityName, entityId, key);
    PropertyEntry entry = entityManager.find(PropertyEntry.class, pk);

    return entry != null;
  }

  public void init(Map config, Map args)
  {
    Object obj = args.get("manager");

    if(obj == null)
    {
      throw new IllegalArgumentException("no manager argument specified");
    }

    if(!(obj instanceof EntityManager))
    {
      throw new IllegalArgumentException("factory specifies is of type '" + obj.getClass() + "' which does not implement " + EntityManager.class.getName());
    }

    this.entityManager = (EntityManager)obj;
    this.entityId = ((Number)args.get("entityId")).longValue();
    this.entityName = (String)args.get("entityName");

    Object tx = args.get("transaction");
    this.transactionType = (tx == null) ? PersistenceUnitTransactionType.RESOURCE_LOCAL : PersistenceUnitTransactionType.valueOf(tx.toString());
  }

  @TransactionAttribute
  public void remove(String key) throws PropertyException
  {
    remove(entityName, entityId, key);
  }

  @TransactionAttribute
  public void remove() throws PropertyException
  {
    remove(entityName, entityId);
  }

  @TransactionAttribute
  public void remove(String entityName, long entityId, String key) throws PropertyException
  {
    EntryPK pk = new EntryPK(entityName, entityId, key);
    PropertyEntry entry = entityManager.find(PropertyEntry.class, pk);

    if(entry != null)
    {
      entityManager.remove(entry);
    }
  }

  @TransactionAttribute
  public void remove(String entityName, long entityId) throws PropertyException
  {
    boolean mustCommit = joinTransaction();
    Query q = entityManager.createNamedQuery("entries");
    q.setParameter("entityId", entityId);
    q.setParameter("entityName", entityName);

    //idiot jalopy blows up on a real man's for loop, so we have to use jdk14 wanky version
    List l = q.getResultList();

    for(Iterator iterator = l.iterator(); iterator.hasNext();)
    {
      Object o = iterator.next();
      entityManager.remove(o);
    }

    if(mustCommit)
    {
      entityManager.getTransaction().commit();
    }
  }

  public boolean supportsType(int type)
  {
    if(type == PROPERTIES)
    {
      return false;
    }

    return true;
  }

  @TransactionAttribute
  protected void setImpl(int type, String key, Object value) throws PropertyException
  {
    setImpl(entityName, entityId, type, key, value);
  }

  @TransactionAttribute
  protected void setImpl(String entityName, long entityId, int type, String key, Object value) throws PropertyException
  {
    EntryPK pk = new EntryPK(entityName, entityId, key);
    PropertyEntry item;

    boolean mustCommit = joinTransaction();

    item = entityManager.find(PropertyEntry.class, pk);

    if(item == null)
    {
      item = new PropertyEntry();
      item.setPrimaryKey(pk);
      item.setType(type);
    }
    else if(item.getType() != type)
    {
      throw new PropertyException("Existing key '" + key + "' does not have matching type of " + type(type));
    }

    switch(type)
    {
      case BOOLEAN:
        item.setBoolValue(((Boolean)value).booleanValue());

        break;

      case INT:
        item.setIntValue(((Number)value).intValue());

        break;

      case LONG:
        item.setLongValue(((Number)value).longValue());

        break;

      case DOUBLE:
        item.setDoubleValue(((Number)value).doubleValue());

        break;

      case STRING:
        item.setStringValue((String)value);

        break;

      case TEXT:
        item.setTextValue((String)value);

        break;

      case DATE:
        item.setDateValue((Date)value);

      case OBJECT:
        item.setSerialized((Serializable)value);

        break;

      case DATA:

        if(value instanceof Data)
        {
          item.setData(((Data)value).getBytes());
        }
        else
        {
          item.setData((byte[])value);
        }

        break;

      case XML:

        String text = writeXML((Document)value);
        item.setTextValue(text);

        break;

      default:
        throw new PropertyException("type " + type + " not supported");
    }

    entityManager.merge(item);
    if(mustCommit)
    {
      entityManager.getTransaction().commit();
    }
  }

  @TransactionAttribute
  public void setAsActualType(String key, Object value) throws PropertyException {
    super.setAsActualType(key, value);
  }

  @TransactionAttribute
  public void setBoolean(String key, boolean value) {
    super.setBoolean(key, value);
  }

  @TransactionAttribute
  public void setDate(String key, Date value) {
    super.setDate(key, value);
  }

  @TransactionAttribute
  public void setData(String key, byte[] value) {
    super.setData(key, value);
  }

  @TransactionAttribute
  public void setDouble(String key, double value) {
    super.setDouble(key, value);
  }

  @TransactionAttribute
  public void setInt(String key, int value) {
    super.setInt(key, value);
  }

  @TransactionAttribute
  public void setLong(String key, long value) {
    super.setLong(key, value);
  }

  @TransactionAttribute
  public void setObject(String key, Object value) {
    super.setObject(key, value);
  }

  @TransactionAttribute
  public void setProperties(String key, Properties value) {
    super.setProperties(key, value);
  }

  @TransactionAttribute
  public void setText(String key, String value) {
    super.setText(key, value);
  }

  @TransactionAttribute
  public void setString(String key, String value) {
    super.setString(key, value);
  }

  @TransactionAttribute
  public void setXML(String key, Document value) {
    super.setXML(key, value);
  }

  protected Object get(int type, String key) throws PropertyException
  {
    return get(entityName, entityId, type, key);
  }

  @TransactionAttribute
  protected Object get(String entityName, long entityId, int type, String key) throws PropertyException
  {
    EntryPK pk = new EntryPK(entityName, entityId, key);
    PropertyEntry entry = entityManager.find(PropertyEntry.class, pk);

    if(entry == null)
    {
      return null;
    }

    if(entry.getType() != type)
    {
      throw new PropertyException("key '" + key + "' does not have matching type of " + type(type) + ", but is of type " + type(entry.getType()));
    }

    switch(type)
    {
      case BOOLEAN:
        return Boolean.valueOf(entry.getBoolValue());

      case DOUBLE:
        return entry.getDoubleValue();

      case STRING:
        return entry.getStringValue();

      case TEXT:
        return entry.getTextValue();

      case LONG:
        return entry.getLongValue();

      case INT:
        return entry.getIntValue();

      case DATE:
        return entry.getDateValue();

      case OBJECT:
        return entry.getSerialized();

      case DATA:
        return entry.getData();

      case XML:
        return readXML(entry.getTextValue());
    }

    throw new PropertyException("type " + type(type) + " not supported");
  }

  private boolean joinTransaction()
  {
    if(inContainer) return false;
    boolean mustCommit = false;

    switch(transactionType)
    {
      case JTA:
        entityManager.joinTransaction();

        break;

      case RESOURCE_LOCAL:

        EntityTransaction tx = entityManager.getTransaction();

        if(!tx.isActive())
        {
          tx.begin();
          mustCommit = true;
        }

        break;
    }

    return mustCommit;
  }

  /**
   * Parse XML document from String in byte array.
   */
  private Document readXML(String data)
  {
    try
    {
      return XMLUtils.parse(data);
    }
    catch(Exception e)
    {
      throw new PropertyImplementationException("Cannot parse XML data", e);
    }
  }

  /**
   * Serialize (print) XML document to byte array (as String).
   */
  private String writeXML(Document doc)
  {
    try
    {
      return XMLUtils.print(doc);
    }
    catch(IOException e)
    {
      throw new PropertyImplementationException("Cannot serialize XML", e);
    }
  }

  public String toString()
  {
    return "EJBPropertySetImpl#" + hashCode() + "{entityManager=" + entityManager + ", entityId=" + entityId + ", entityName='" + entityName + '\'' + ", inContainer=" + inContainer + '}';
  }
}
