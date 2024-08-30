type BreadCrumbData = {
    title: string;
    href: string;
};

// Takes a pathname of the form /a/b/c and transforms it into
// an array of BreadCrumbData used for the breadcrumb component
export const createBreadCrumbDataFromPathName = (pn: string) => {
    if (pn === "/") {
        return [];
    }
    const names = pn.split("/").slice(1);
    return names.reduce((prev, curr, idx) => {
        if (prev.length === 0) {
            return [{ title: curr, href: "/" + curr }];
        }
        const prevHref = prev[idx - 1].href;
        return [...prev, { title: curr, href: prevHref + "/" + curr }];
    }, [] as BreadCrumbData[]);
};
