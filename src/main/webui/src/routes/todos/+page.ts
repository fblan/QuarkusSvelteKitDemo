import type { PageLoad } from './$types';



export async function load ()  {
    const response = await fetch("/rest/todo/all")
    const todos =  await response.json();
    return {todos};
}
