# spring cache

[TOC]

当我们在调用一个缓存方法时会根据相关信息和返回结果作为一个键值对存放在缓存中，等到下次利用同样的参数来调用该方法时将不再执行该方法，而是直接从缓存中获取结果进行返回。

## 注解

spring 对 cache 提供了支持，首先了解下几个注解

缓存的操作由3个注解 @Cacheable，@CachePut，@CacheEvict构成，这3个注解中的参数几乎是一样的，平常业务代码也主要用这几个注解即可。

### @Cacheable，@CachePut，@CacheEvict

- @Cacheable
被这个注解修饰的方法，首先去取缓存，如果没有缓存，则执行方法并将结果缓存。
- @CachePut
被这个注解修饰的方法，执行方法，并将结果缓存。
- @CacheEvict
被这个注解修饰的方法，执行方法，并将对应的缓存失效。

这些注解有一些相同含义的属性：
- value 缓存块的名称
- cacheNames 缓存块的名称，同value
- key 缓存的key，SpEL动态计算key
- keyGenerator key生成方式，**和key属性互斥**
- cacheManager 缓存管理器 bean 的名称。当 CacheResolver 的 bean 不存在时，会用 CacheManager 创建；这个管理器用来生成 CacheResolver 对象；**和cacheResolver属性互斥**
- cacheResolver
- condition SpEL 会计算 condition 的表达式，对方法的返回结果有条件的缓存

以下属性只有@Cacheable，@CachePut有：
- unless SpEL 会计算 unless 的表达式，对方法的返回结果有条件的 不缓和；和condition 语义相反
- sync

以下属性只有@CachePut有：
- allEntries 移除缓存块中的所有key
- beforeInvocation 是否在方法执行之前失效缓存

### @Caching

@Cacheable，@CachePut，@CacheEvict一组操作的集合

### @CacheConfig

主要用于配置该类中会用到的一些共用的缓存配置，公用的配置是：
- cacheNames
- keyGenerator
- cacheManager
- cacheResolver

### @EnableCaching
开启缓存注解

## 核心接口

spring对cache的支持在 spring-context包中，定义了2个接口：Cache 和 CacheManager

CacheManager 用来管理Cache

```java
public interface CacheManager {

	/**
	 * 通过Cahce的名称获取Cache
	 */
	@Nullable
	Cache getCache(String name);

	/**
	 * 获取当前管理的所有Cache的名称
	 */
	Collection<String> getCacheNames();

}
```

Cache是缓存块，定义了一组对Cache操作的方法

```java
public interface Cache {

	/**
	 * 返回当前Cache的名称
	 */
	String getName();

	/**
	 * ??
	 */
	Object getNativeCache();

	/**
	 * 在当前Cache中，通过key，返回value
	 */
	@Nullable
	ValueWrapper get(Object key);

	/**
	 * 在当前Cache中，通过key，返回value
	 */
	@Nullable
	<T> T get(Object key, @Nullable Class<T> type);

	/**
	 * 原文：
	 * This method provides
	 * a simple substitute for the conventional "if cached, return; otherwise
	 * create, cache and return" pattern.
	 * 即：如果key对应的缓存命中，返回value，否则创建key对应的缓存，并返回value
	 */
	@Nullable
	<T> T get(Object key, Callable<T> valueLoader);

	/**
	 * 将入参kv对放入Cache中
	 */
	void put(Object key, @Nullable Object value);

	/**
	 * <p>This is equivalent to:
	 * <pre><code>
	 * Object existingValue = cache.get(key);
	 * if (existingValue == null) {
	 *     cache.put(key, value);
	 *     return null;
	 * } else {
	 *     return existingValue;
	 * }
	 * </code></pre>
	 */
	@Nullable
	ValueWrapper putIfAbsent(Object key, @Nullable Object value);

	/**
	 * 清除key对应的kv数据
	 */
	void evict(Object key);

	/**
	 * 清除Cache中的所有kv数据
	 */
	void clear();


	/**
	 * 对Cache中的value的包装
	 */
	@FunctionalInterface
	interface ValueWrapper {

		/**
		 * 返回value对象
		 */
		@Nullable
		Object get();
	}


	/**
	 * 定义了一个异常
	 */
	@SuppressWarnings("serial")
	class ValueRetrievalException extends RuntimeException {

		@Nullable
		private final Object key;

		public ValueRetrievalException(@Nullable Object key, Callable<?> loader, Throwable ex) {
			super(String.format("Value for key '%s' could not be loaded using '%s'", key, loader), ex);
			this.key = key;
		}

		@Nullable
		public Object getKey() {
			return this.key;
		}
	}

}
```

## 实现原理

看3个实现：
1. NoOp
2. ConcurrentMap
3. Redis

## NoOp 实现

NoOp 是无任何涉及缓存的操作
NoOp涉及到2个类：NoOpCache和 NoOpCacheManager

## ConcurrentMap 实现
ConcurrentMap是由ConcurrentMapCacheManager和ConcurrentMapCache实现的

### AbstractValueAdaptingCache

ConcurrentMapCache继承**AbstractValueAdaptingCache**类，AbstractValueAdaptingCache类是在底层存储之前，适应null值，比如ConcurrentHashMap，不支持null值的情况，进行一种包装。

抽象类AbstractValueAdaptingCache有一个私有字段allowNullValues，表示是否运营null值，通过构造器传入，并提供查询方法。

**toStoreValue**方法将当前的数据（userValue）转化为底层存储的数据（storeValue）。如果不支持null值（allowNullValues=false），那么传入的数据如果为null将会抛出IllegalArgumentException异常；如果支持null值（allowNullValues=true），那么传入的数据如果为null，将会转成对象NullValue.INSTANCE。
这个转化可以适配ConcurrentHashMap这样子不支持null值的情况。

**fromStoreValue**方法是toStoreValue的逆过程。如果storeValue是NullValue.INSTANCE，并且支持null值，那么返回null；否则返回storeValue

1. 当userValue!=null时 userValue==storeValue
2. 当userValue==null时 
- allowNullValues==true storeValue==NullValue.INSTANCE
- allowNullValues==false，toStoreValue方法抛出IllegalArgumentException异常，阻止数据存入


**protected abstract Object lookup(Object key);**方法由实现类提供一种查找key对应的value的方法，返回的value是storeValue，如果返回null表示cache中没有这个key

**toValueWrapper**方法将storeValue转换成Cache.ValueWrapper对象，Cache.ValueWrapper前面讲过，是对value的一种包装，SimpleValueWrapper是Cache.ValueWrapper的一种实现；底层存储的数据 storeValue 不可能为null，因为经过了toStoreValue的处理；但是lookup返回的数据可能为null，因为key可能不在底层存储中

AbstractValueAdaptingCache另外有2个get方法，看下实现：

```java
	/**
	 * 通过key在cache中查找value，返回类型是 ValueWrapper
	 */
	@Override
	public ValueWrapper get(Object key) {
		Object value = lookup(key);
		return toValueWrapper(value);
	}

	/**
	 * 通过key在cache中查找value，返回类型强制转换成type类型
	 */
	@Override
	@Nullable
	public <T> T get(Object key, @Nullable Class<T> type) {
		Object value = fromStoreValue(lookup(key));
		// 强制转换之前先座一次类型校验，类型不一致抛出IllegalStateException异常
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException(
					"Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		return (T) value;
	}
```

**问题** 如果type==null，会发生什么情况？

### ConcurrentMapCache

ConcurrentMapCache 有3个私有字段：

```java
/**
 * cache name，cacheManager通过name获取cache
 */
private final String name;

/**
 * native cache，真正存储kv的地方，实现是ConcurrentHashMap
 */
private final ConcurrentMap<Object, Object> store;

/**
 * 序列化/反序列化 方式
 */
private final SerializationDelegate serialization;
```

serialization 这个字段很有意思。ConcurrentHashMap的实现是一种**local cache**
 1. 如果serialization==null的话，**存储的是对象的引用。这时如果返回之后应用对对象进行修改操作，那么缓存中存储的value将会随着修改**
 2. 如果serialization!=null的话，**存储的是对象的copy。这时如果返回之后应用对对象进行修改操作，那么缓存中存储的value不会修改**

为什么local cache中的value会有这种情况，而key没有呢？因为java中string是不可变的，而key都是string类型。

ConcurrentMapCache 提供了3个共有构造器有1个受保护的构造器，实现都很简单

```java
	public ConcurrentMapCache(String name) {
		this(name, new ConcurrentHashMap<>(256), true);
	}

	public ConcurrentMapCache(String name, boolean allowNullValues) {
		this(name, new ConcurrentHashMap<>(256), allowNullValues);
	}
	
	public ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
		this(name, store, allowNullValues, null);
	}

	protected ConcurrentMapCache(String name, ConcurrentMap<Object, Object> store,
			boolean allowNullValues, @Nullable SerializationDelegate serialization) {

		super(allowNullValues);
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(store, "Store must not be null");
		this.name = name;
		this.store = store;
		this.serialization = serialization;
	}
```

**isStoreByValue**方法判断当前的ConcurrentMapCache存储的value是对象的引用还是对象的copy。true表示对象的copy；false表示对象的引用
因为SerializationDelegate serialization字段只能通过受保护的构造器传入，并且没有提供setter方法，所以**当前的ConcurrentMapCache作为一种local cache存储的value都是对象的引用，无法通过序列化/反序列化方式存储对象的copy**。

**private Object serializeValue(SerializationDelegate serialization, Object storeValue) throws IOException**
这个方法用SerializationDelegate将storeValue进行序列化，实际返回的是byte数组
**private Object deserializeValue(SerializationDelegate serialization, Object storeValue) throws IOException**
这个方法用SerializationDelegate将byte数组反序列化对对象，storeValue参数实际上是byte数组

这个两个方法可以看成
```java
private byte[] serializeValue(SerializationDelegate serialization, Object storeValue) throws IOException;

private Object deserializeValue(SerializationDelegate serialization, byte[] storeValue) throws IOException
```

ConcurrentMapCache重写了父类AbstractValueAdaptingCache的toStoreValue和fromStoreValue方法

```java
	/**
	 * 1. 用父类提供的 toStoreValue 方法将 userValue 转换成 storeValue
	 * 2. 如果 serialization!=null，将storeValue序列化并返回
	 */
	@Override
	protected Object toStoreValue(@Nullable Object userValue) {
		Object storeValue = super.toStoreValue(userValue);
		if (this.serialization != null) {
			try {
				return serializeValue(this.serialization, storeValue);
			}
			catch (Throwable ex) {
				throw new IllegalArgumentException("Failed to serialize cache value '" + userValue +
						"'. Does it implement Serializable?", ex);
			}
		}
		else {
			return storeValue;
		}
	}

	/**
	 * 1. 如果storeValue!=null 并且 this.serialization!=null，进行反序列化
	 * 2. 用父类的fromStoreValue方法返回结果
	 */
	@Override
	protected Object fromStoreValue(@Nullable Object storeValue) {
		if (storeValue != null && this.serialization != null) {
			try {
				return super.fromStoreValue(deserializeValue(this.serialization, storeValue));
			}
			catch (Throwable ex) {
				throw new IllegalArgumentException("Failed to deserialize cache value '" + storeValue + "'", ex);
			}
		}
		else {
			return super.fromStoreValue(storeValue);
		}

	}
```

所以在ConcurrentHashMap中存储value的方式：
**toStoreValue->serializeValue->map.put**
查询结果返回value的方式：
**deserializeValue->fromStoreValue**
serializeValue和deserializeValue只有在serialization字段不为null的时候执行

**getName**返回cache name

**getNativeCache**返回用来存储kv的ConcurrentHashMap

**protected Object lookup(Object key)**返回storeValue，这个storeValue可以为null（ConcurrentHashMap不存在这个key）；这个方法是父类AbstractValueAdaptingCache要求实现的。实际的实现是ConcurrentHashMap的get方法

**public <T> T get(Object key, Callable<T> valueLoader)**方法
问题：查一下实现computeIfAbsent，jdk1.8提供的，并给出1.7的实现

**public void put(Object key, @Nullable Object value)**存储kv对

**public ValueWrapper putIfAbsent(Object key, @Nullable Object value)**如果ConcurrentHashMap中没有的话，将value存入

**public void evict(Object key)**失效，实际的实现的ConcurrentHashMap中的key

**public void clear()**清除当前cache的所有key，实际的实现是ConcurrentHashMap的clear方法