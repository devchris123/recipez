import { createBreadCrumbDataFromPathName } from "./createBreadCrumbData";

it("Should transform a pathname into an array of bread crumb data", () => {
    // setup
    const testData = [
        {
            pathname: "/recipes/hello/world", expected: [
                { title: "recipes", href: "/recipes" },
                { title: "hello", href: "/recipes/hello" },
                { title: "world", href: "/recipes/hello/world" }
            ]
        }, {
            pathname: "/recipes", expected: [
                { title: "recipes", href: "/recipes" },
            ]
        },
        {
            pathname: "/", expected: []
        }
    ];

    // execute / assert
    for (const td of testData) {
        expect(createBreadCrumbDataFromPathName(td.pathname)).toEqual(td.expected)
    }
})