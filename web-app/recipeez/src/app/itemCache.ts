export const cacheItem = (key: string, value: string) => {
    localStorage.setItem(key, value);
};

export const getCachedItem = (key: string) => {
    return localStorage.getItem(key);
};

export const rmCachedItem = (key: string) => {
    localStorage.removeItem(key);
}

