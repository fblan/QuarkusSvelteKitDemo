<script lang="ts">

    import ToDoEdit from "./ToDoEdit.svelte";
    import {invalidate, invalidateAll} from "$app/navigation";

    export let todo;

    $:console.log("todo",todo);

    let edition:boolean=false;

    function setEditionMode(){
        edition = true;
    }
    function setReadMode(){
        console.log("callback called")
        edition = false;
        invalidate('todos');
    }

</script>

{#if edition}
    <ToDoEdit todo={todo} callback={setReadMode}/>
{:else }
   {todo.text}<button on:click={setEditionMode}>edit</button>
{/if}

